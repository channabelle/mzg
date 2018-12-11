var ComponentsDateTimePickers = function () {

    var handleDatePickers = function () {


        if (jQuery().datepicker) {
            $('.date-picker').datepicker({
                rtl: App.isRTL(),
                orientation: "left",
                autoclose: true
            });
            //$('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
        }

        /* Workaround to restrict daterange past date select: http://stackoverflow.com/questions/11933173/how-to-restrict-the-selectable-date-ranges-in-bootstrap-datepicker */
        // Workaround to fix datepicker position on window scroll
        $(document).scroll(function () {
            $('#form_modal2 .date-picker').datepicker('place'); //#modal is the id of the modal
        });
    }

    var handleTimePickers = function () {

        if (jQuery().timepicker) {
            $('.timepicker-default').timepicker({
                autoclose: true,
                showSeconds: true,
                minuteStep: 1
            });

            $('.timepicker-no-seconds').timepicker({
                autoclose: true,
                minuteStep: 5
            });

            $('.timepicker-24').timepicker({
                autoclose: true,
                minuteStep: 5,
                showSeconds: false,
                showMeridian: false
            });

            // handle input group button click
            $('.timepicker').parent('.input-group').on('click', '.input-group-btn', function (e) {
                e.preventDefault();
                $(this).parent('.input-group').find('.timepicker').timepicker('showWidget');
            });

            // Workaround to fix timepicker position on window scroll
            $(document).scroll(function () {
                $('#form_modal4 .timepicker-default, #form_modal4 .timepicker-no-seconds, #form_modal4 .timepicker-24').timepicker('place'); //#modal is the id of the modal
            });
        }
    }

    var handleDateRangePickers = function () {
        if (!jQuery().daterangepicker) {
            return;
        }

        // $('#defaultrange').daterangepicker({
        //     opens: (App.isRTL() ? 'left' : 'right'),
        //     format: 'MM/DD/YYYY',
        //     separator: ' to ',
        //     startDate: moment().subtract('days', 6),
        //     endDate: moment(),
        //     ranges: {
        //         'Today': [moment(), moment()],
        //         'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
        //         'Last 7 Days': [moment().subtract('days', 6), moment()],
        //         'Last 30 Days': [moment().subtract('days', 29), moment()],
        //         'This Month': [moment().startOf('month'), moment().endOf('month')],
        //         'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
        //     },
        //     minDate: '01/01/2012',
        //     maxDate: '12/31/2018',
        // }, function (start, end) {
        //     $('#defaultrange input').val(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
        // });

        // $('#defaultrange_modal').daterangepicker({
        //     opens: (App.isRTL() ? 'left' : 'right'),
        //     format: 'MM/DD/YYYY',
        //     separator: ' to ',
        //     startDate: moment().subtract('days', 29),
        //     endDate: moment(),
        //     minDate: '01/01/2012',
        //     maxDate: '12/31/2018',
        // }, function (start, end) {
        //     $('#defaultrange_modal input').val(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
        // });

        // this is very important fix when daterangepicker is used in modal. in modal when daterange picker is opened and mouse clicked anywhere bootstrap modal removes the modal-open class from the body element.
        // so the below code will fix this issue.
        $('#defaultrange_modal').on('click', function () {
            if ($('#daterangepicker_modal').is(":visible") && $('body').hasClass("modal-open") == false) {
                $('body').addClass("modal-open");
            }
        });

        var _time_start = (null == REG_EXP.getURLParam('_cTime_start')) ?
            moment().startOf('month') : moment(REG_EXP.getURLParam('_cTime_start', 'YYYY-MM-DD'));
        var _time_end = (null == REG_EXP.getURLParam('_cTime_end')) ?
            moment().endOf('month') : moment(REG_EXP.getURLParam('_cTime_end', 'YYYY-MM-DD'));

        $('#reportrange').daterangepicker({
            opens: (App.isRTL() ? 'left' : 'right'),

            startDate: _time_start,
            endDate: _time_end,
            //minDate: '01/01/2012',
            //maxDate: '12/31/2014',
            dateLimit: {
                days: 60
            },
            showDropdowns: true,
            showWeekNumbers: true,
            timePicker: false,
            timePickerIncrement: 1,
            timePicker12Hour: true,
            ranges: {
                '今天': [moment(), moment()],
                '昨天': [moment().subtract('days', 1), moment().subtract('days', 1)],
                '最近7日': [moment().subtract('days', 6), moment()],
                '最近30日': [moment().subtract('days', 29), moment()],
                '本月': [moment().startOf('month'), moment().endOf('month')],
                '上月': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
            },
            buttonClasses: ['btn'],
            applyClass: 'green',
            cancelClass: 'default',
            format: 'YYYY/MM/DD',
            separator: ' to ',
            locale: {
                cancelLabel: '取消',
                applyLabel: '确定',
                fromLabel: 'From',
                toLabel: 'To',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        }, function (start, end) {

            console.log('start: ' + start + ", end: " + end);

            $('#reportrange .input_dateRange_start').html(start.format('YYYY-MM-DD'));
            $('#reportrange .input_dateRange_to').html(' - ');
            $('#reportrange .input_dateRange_end').html(end.format('YYYY-MM-DD'));
        });
        //Set the initial state of the picker label
        $('#reportrange .input_dateRange_start').html(_time_start.format('YYYY-MM-DD'));
        $('#reportrange .input_dateRange_to').html(' - ');
        $('#reportrange .input_dateRange_end').html(_time_end.format('YYYY-MM-DD'));
    }

    var handleDatetimePicker = function () {


        if (!jQuery().datetimepicker) {
            return;
        }

        $(".form_datetime").datetimepicker({
            autoclose: true,
            isRTL: App.isRTL(),
            format: "dd MM yyyy - hh:ii",
            pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left")
        });

        $(".form_advance_datetime").datetimepicker({
            isRTL: App.isRTL(),
            format: "dd MM yyyy - hh:ii",
            autoclose: true,
            todayBtn: true,
            startDate: "2013-02-14 10:00",
            pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left"),
            minuteStep: 10
        });

        $(".form_meridian_datetime").datetimepicker({
            isRTL: App.isRTL(),
            format: "dd MM yyyy - HH:ii P",
            showMeridian: true,
            autoclose: true,
            pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left"),
            todayBtn: true
        });

        $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal

        // Workaround to fix datetimepicker position on window scroll
        $(document).scroll(function () {
            $('#form_modal1 .form_datetime, #form_modal1 .form_advance_datetime, #form_modal1 .form_meridian_datetime').datetimepicker('place'); //#modal is the id of the modal
        });
    }

    var handleClockfaceTimePickers = function () {


        if (!jQuery().clockface) {
            return;
        }

        $('.clockface_1').clockface();

        $('#clockface_2').clockface({
            format: 'HH:mm',
            trigger: 'manual'
        });

        $('#clockface_2_toggle').click(function (e) {
            e.stopPropagation();
            $('#clockface_2').clockface('toggle');
        });

        $('#clockface_2_modal').clockface({
            format: 'HH:mm',
            trigger: 'manual'
        });

        $('#clockface_2_modal_toggle').click(function (e) {
            e.stopPropagation();
            $('#clockface_2_modal').clockface('toggle');
        });

        $('.clockface_3').clockface({
            format: 'H:mm'
        }).clockface('show', '14:30');

        // Workaround to fix clockface position on window scroll
        $(document).scroll(function () {
            $('#form_modal5 .clockface_1, #form_modal5 #clockface_2_modal').clockface('place'); //#modal is the id of the modal
        });
    }

    return {
        //main function to initiate the module
        init: function () {
            // handleDatePickers();
            // handleTimePickers();
            // handleDatetimePicker();
            handleDateRangePickers();
            // handleClockfaceTimePickers();
        }
    };

}();

if (App.isAngularJsApp() === false) {
    jQuery(document).ready(function () {
        ComponentsDateTimePickers.init();
    });
}