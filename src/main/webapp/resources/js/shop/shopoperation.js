//完成从数据库读取数据到前端页面，以及前端页面填写的数据到后台的过程

//加载完前端页面后执行以下操作

$(function () {
    iniUrl = '/o2o/shopadmin/getshopinitinfo';
    registerUrl = '/o2o/shopadmin/registershop';
    getShopInitInfo();
    submit();

    //获取下拉列表初始信息的函数
    function getShopInitInfo() {
        //使用Ajax 请求获取 JSON 数据,并将结果data输入到回调函数function中, 初始化表单内容
        $.getJSON(iniUrl, function (data) {
            if (data.success) {
                let tempHtml = '';
                let tempAreaHtml = '';
                //遍历shopCategoryList
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">' +
                        item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">' +
                        item.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
    }

    //提交表单信息的函数
    function submit() {
        $('#submit').click(function () {
            var shop = {};
            shop.shopName = $('#shop-name').val();
            shop.shopAddr = $('#shop-addr').val();
            shop.phone = $('#shop-phone').val();
            shop.shopDesc = $('#shop-desc').val();
            shop.shopCategory = {
                //shopCategoryId为option下选中的shop的id
                shopCategoryId: $('#shop-category').find('option').not(function () {
                    return !this.selected;
                }).data('id')
            };
            shop.area = {
                areaId: $('#area').find('option').not(function () {
                    return !this.selected;
                }).data('id')
            };
            //读取图片流
            var shopImg = $('#shop-img')[0].files[0];
            //将所有数据存储到formData中，以ajax形式发送到后端
            var formData = new FormData();
            formData.append('shopImg', shopImg);
            formData.append('shopStr', JSON.stringify(shop));
            var verifyCodeInput = $('#j_captcha').val();
            if (!verifyCodeInput) {
                //如果未输入验证码
                $.toast("请输入验证码!");
                return;
            }
            formData.append("verifyCodeInput", verifyCodeInput);
            $.ajax({
                url: registerUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                //执行成功后将结果放入function中
                success: function (data) {
                    if (data.success) {
                        $.toast('提交成功!');
                    } else {
                        $.toast('提交失败!' + data.errMsg);
                    }
                    //更换验证码
                    $('#captcha_img').click();
                }
            })
        });
    }
})