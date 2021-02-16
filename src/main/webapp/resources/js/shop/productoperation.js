$(function () {
    var productId = getQueryString('productId');
    var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
    var categoryUrl = '/o2o/shopadmin/getproductcategorylist'; //shopId是从session中取出的,需要提前访问getshopmanagementinfo
    var productPostUrl = '/o2o/shopadmin/modifyproduct';
    var isEdit = false;
    if (productId) {
        //如果获得productId信息，则说明是对product信息进行修改
        //从后端获取到product的相关信息，并展示到前端页面
        getInfo(productId);
        isEdit = true;
    } else {
        //如果没有获得product信息，则获得商品的类别目录, productPostUrl变为addproduct
        getCategory();
        productPostUrl = '/o2o/shopadmin/addproduct';
    }

    function getInfo(productId) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);

                var optionHtml = '';
                var optionArr = data.productCategoryList;
                var optionSelected = product.productCategory.productCategoryId;
                optionArr.map(function (item, index) {
                    //预选定的option
                    var isSelect = optionSelected === item.productCategoryId ? 'selected' : '';
                    optionHtml += '<option data-value="'
                        + item.productCategoryId
                        + '"'
                        + isSelect
                        + '>'
                        + item.productCategoryName
                        + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }

    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if (data.success) {
                var productCategoryList = data.data;
                var optionHtml = '';
                productCategoryList.map(function (item,index){
                    optionHtml += '<option data-value="'
                        + item.productCategoryId + '">'
                        + item.productCategoryName + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }

    //针对商品详情图的控件组，若该控件组的最后一个元素发生变化(上传图片)
    //且控件总数<6，则生成一个新的文件上传控件
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });

    $('#submit').click(
        function () {
            var product = {};
            product.productName = $('#product-name').val();
            product.productDesc = $('#product-desc').val();
            product.priority = $('#priority').val();
            product.normalPrice = $('#normal-price').val();
            product.promotionPrice = $('#promotion-price').val();
            product.productCategory = {
                productCategoryId: $('#category').find('option').not(
                    function () {
                        return !this.selected;
                    }).data('value')
            };
            product.productId = productId;
            //获取缩略值文件流
            var thumbnail = $('#small-img')[0].files[0];
            console.log(thumbnail);
            //生成表单对象，用于接受图片、product信息并传递给后台
            var formData = new FormData();
            formData.append('thumbnail', thumbnail);
            $('.detail-img').map(function (index, item) {
                    //对图片控件进行遍历，如果控件选择了文件
                    if ($('.detail-img')[index].files.length > 0){
                        //将第i个文件流赋值给key为productImgi的表单键值对中
                        formData.append('productImg' + index, $('.detail-img')[index].files[0]);
                    }
                });
            formData.append('productStr', JSON.stringify(product));
            //获取表单中填写的验证码
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            }
            formData.append("verifyCodeActual", verifyCodeActual);
            //将数据提交到后台进行相关处理
            $.ajax({
                url: productPostUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.success) {
                        $.toast('提交成功！');
                        //刷新验证码
                        $('#captcha_img').click();
                    } else {
                        $.toast('提交失败！');
                        $('#captcha_img').click();
                    }
                }
            });
        });
});