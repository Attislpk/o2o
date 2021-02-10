/**
 *
 * @param img 验证码图片
 */
function changeVerifyCode(img){
    // servlet对应的映射路径为： /Kaptcha, 因此以下路径会随机生成一个图片验证码
    img.src="../Kaptcha?" + Math.floor(Math.random()*100);
}


/**
 * 判断是否传入了shopId
 * @param name shopId
 * @returns {string}
 */
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return '';
}