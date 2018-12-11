package com.channabelle.common.utils.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.region.Region;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TencentCOS {
    private static Logger log = Logger.getLogger(TencentCOS.class);
    private COSClient client = null;

    private static final String DEFAULT_region = "ap-shanghai";
    private static final String DEFAULT_bucketName = "aa" + "-" + TencentCloud.APPID;
    private static final String DEFAULT_CDN_PROTOCAL = "https://";
    private static final String DEFAULT_CDN_DOMAIN_POSTFIX = ".file.myqcloud.com/";

    public TencentCOS() {
        this(TencentCloud.SecretId, TencentCloud.SecretKey, TencentCOS.DEFAULT_region);
    }

    public TencentCOS(String SecretId, String SecretKey, String region) {
        log.info(String.format("TencentCOS SecretId: %s, SecretKey: %s, region: %s", SecretId, SecretKey, region));

        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(SecretId, SecretKey);
        // 2 设置bucket的区域, COS地域的简称请参照
        // https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        client = new COSClient(cred, clientConfig);
    }

    private COSClient getClient() {
        return client;
    }

    public void closeClient() {
        this.getClient().shutdown();
    }

    private String getBucketLocation() {
        return getBucketLocation(TencentCOS.DEFAULT_bucketName);
    }

    private String getBucketLocation(String bucketName) {
        return this.getClient().getBucketLocation(bucketName);
    }

    private boolean doesBucketExist() {
        return doesBucketExist(TencentCOS.DEFAULT_bucketName);
    }

    private boolean doesBucketExist(String bucketName) {
        return this.getClient().doesBucketExist(bucketName);
    }

    private ObjectListing listObjects() {
        return listObjects(TencentCOS.DEFAULT_bucketName);
    }

    private ObjectListing listObjects(String bucketName) {
        return this.getClient().listObjects(bucketName);
    }

    public List<JSONObject> listAll() {
        return listAll(TencentCOS.DEFAULT_bucketName);
    }

    public List<JSONObject> listAll(String bucketName) {
        return listAll(bucketName, null);
    }

    public List<JSONObject> listAll(String bucketName, String root_folder) {
        log.info(String.format("listAll bucketName: %s, root_folder: %s", bucketName, root_folder));

        List<JSONObject> array = new ArrayList<JSONObject>();

        ObjectListing objectListing = this.listObjects(bucketName);
        log.info("doesBucketExist: " + this.doesBucketExist());
        log.info("getBucketLocation: " + this.getBucketLocation());

        // 获取下次 list 的 marker
        String nextMarker = objectListing.getNextMarker();
        log.info("nextMarker: " + nextMarker);

        // 判断是否已经 list 完, 如果 list 结束, 则 isTruncated 为 false, 否则为 true
        boolean isTruncated = objectListing.isTruncated();
        log.info("isTruncated: " + isTruncated);

        List<COSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for(COSObjectSummary cosObjectSummary : objectSummaries) {
            // 文件路径
            String key = cosObjectSummary.getKey();
            // 获取文件长度
            long fileSize = cosObjectSummary.getSize();
            // 获取文件ETag
            String eTag = cosObjectSummary.getETag();
            // 获取最后修改时间
            Date lastModified = cosObjectSummary.getLastModified();
            // 获取文件的存储类型
            String StorageClassStr = cosObjectSummary.getStorageClass();

            if(null == root_folder || 0 == root_folder.length() || true == key.startsWith(root_folder + "/")) {
                JSONObject j = new JSONObject();
                j.put("key", key);
                j.put("path", DEFAULT_CDN_PROTOCAL + bucketName + DEFAULT_CDN_DOMAIN_POSTFIX + key);
                j.put("fileSize", fileSize);
                j.put("eTag", eTag);
                j.put("lastModified", lastModified.getTime());
                j.put("StorageClassStr", StorageClassStr);
                array.add(j);
            }
        }
        return array;
    }
}
