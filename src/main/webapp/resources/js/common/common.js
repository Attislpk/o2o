
function changeVerifyCode(img){
    // servlet对应的映射路径为： /Kaptcha, 因此以下路径会随机生成一个图片验证码
    img.src="../Kaptcha?" + Math.floor(Math.random()*100);
}