$(function(){
    var shopId = getQueryString('shopId');
    var shopInfoUrl = '/o2o/shopadmin/getshopmanagementinfo?shopId=' + shopId;

    $.getJSON(shopInfoUrl, function(data){
        if(data.redirect){
            window.location.href=data.url;
        } else{
            if(data.shopId != undefined && data.shopId != null){
                shopId = data.shopId;
            }
            //如果有shopId时，点击shopInfo是修改店铺信息，要不然就是注册（默认）
            $('#shopInfo').attr('href', '/o2o/shopadmin/shopoperation?shopId=' + shopId);
        }
    });
});