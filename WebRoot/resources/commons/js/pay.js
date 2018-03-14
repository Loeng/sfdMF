function orderPay(orderId, orderType, payType) {
    var webroot = $("#webroot").val()
    layer.open({
        type: 2,
        title: '订单支付',
        skin: 'payZone-class',
        area: ['600px', '385px'], //宽高
        content: [webroot + "/store/sfdorder/payload/" + orderId + "/" + orderType + "/" + payType, 'no'],
        cancel: function () {
            window.location.reload();
        }
    });
}