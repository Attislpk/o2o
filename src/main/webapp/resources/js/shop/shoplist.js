$(function () {
    getlist();


    function getlist(){
        $.ajax({
            url:"/o2o/shopadmin/getshoplist",
            type:"get",
            dataType:"json",
            success:function (data){
                if (data.success){
                    handleList(data.shopList);
                    handleUser(data.user);
                }
            }
        });
    }

    function handleUser(user){
        $('#user-name').text(user.userName);
    }

    function handleList(shopList){
        var html = "";
        //将获取到的shopList逐行遍历显示,每一行是一个shop信息
            var html = '';
            shopList.map(function(item, index){
                html += '<div class="row row-shop"><div class="col-40">'
                    + item.shopName +'</div><div class="col-40">'
                    + shopStatus(item.status)
                    +'</div><div class="col-20">'
                    + goShop(item.status, item.shopId) + '</div></div>';
            });
            //将shop信息追加到shop-warp中，并显示到html控件
            $('.shop-wrap').html(html);
        }

    function shopStatus(status){
        if (status === 0){
            return '审核中';
        }else if (status === -1){
            return '店铺非法';
        }else if (status === 1){
            return '审核通过';
        }
    }

    function goShop(status,shopId){
        if (status === 1){
            return '<a href="/o2o/shopadmin/shopmanagement?shopId='+shopId+'">进入</a>'
        }else {
            return "";
        }
    }
});