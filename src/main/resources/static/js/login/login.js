
var LoginApp = new Vue({
    el:'#login',
    data:{
        //注册 登录 加载值
        which:"loading", // "Login":加载登录页; "Register":加载注册页;"loading":加载loading页

        //注册输入框提示信息及验证
        UserNameEmpty:false,
        EnterPassword:false,
        PsdDiffrient:false,
        EnterRealName:false,
        EnterPhoneNo:false,
        EnterIp:false,

        //登录输入框提示信息及验证
        loginNameEmpty:false,
        loginPsd:false,
        loginIp:false,

        //封装用户登录信息对象
        UserStorage:{},
        //服务器IP地址存储
        IPstorage:'',
    },
    methods:{
        //切换登录注册
        ToLogin:function(){
            this.which = 'Login';
        },
        ToReg:function(){
            this.which = 'Register';
        },
        forgetPsd:function(){
            webToast('请联系管理员重置密码','middle',2000);
        },
        //注册按钮
        Register:function(){
            var username = $("#enterUser").val();        //获取注册用户名
            var psd = $("#erterpassword").val();         //获取注册密码
            var comfirmpsd = $("#confirmpassword").val();//获取注册确认密码
            var realname = $("#erterName").val();        //获取注册真实姓名
            var phone = $("#erterNumber").val();         //获取注册手机号码
            var ipA = $("#enterIp").val();               //获取服务器IP地址
            
            //注册信息验证
            if(username == ''){
                this.UserNameEmpty = true;
            }else{
                this.UserNameEmpty = false;
            }
            if(psd == ''){
                this.EnterPassword = true;
            }else{
                this.EnterPassword = false;
            }
            if(comfirmpsd == psd && comfirmpsd !== ''){
                this.PsdDiffrient = false;
            }else{
                this.PsdDiffrient = true;
            }
            if(realname == ''){
                this.EnterRealName = true;
            }else{
                this.EnterRealName = false;
            }
            if(phone == '' || !(/^1[34578]\d{9}$/.test(phone))){
                this.EnterPhoneNo = true;
            }else {
                this.EnterPhoneNo = false;
            }
            if(ipA == ''){
                this.EnterIp = true;
            }else{
                this.EnterIp = false;
            }
        
            //验证通过，发送注册请求
            if(ipA !== '' && username !== '' && psd !== '' && comfirmpsd == psd && comfirmpsd !== '' && realname !== '' && phone !== '' && (/^1[34578]\d{9}$/.test(phone))){
                //封装注册必要数据对象
                var regObj = {
                    'loginName':username,
                    'password':psd,
                    'name':realname,
                    'phone':phone
                };
                $.ajax({
                    url:'http://'+ ipA +':8168/rs/user/register',
                    type: "GET",
                    async: true,
                    data:{
                        registerInfo:JSON.stringify(regObj)
                    },
                    dataType:"json",
                    contentType:"application/json",
                    error:function(){
                        webToast('注册失败','middle',3000);
                    },
                    success:function(data){
                        webToast(data,'middle',2000);
                        if(data == '该登录名或手机号已存在'){
                            console.log(data);
                        }else{
                            setTimeout(function(){
                                LoginApp.which = "Login";
                                $("#ipadress").val(ipA);
                            },2000)
                        }
                    }
                })
            }     
        },
        //登录按钮
        Login:function(){
            var loginusername = $("#userName").val();        //获取登录用户名
            var loginpsd = $("#password").val();         //获取登录密码
            var loginIpAdr = $("#ipadress").val();         //获取服务器Ip
            
            //登录信息验证
            if(loginusername == ''){
                this.loginNameEmpty = true;
            }else{
                this.loginNameEmpty = false;
            }
            if(loginpsd == ''){
                this.loginPsd = true;
            }else{
                this.loginPsd = false;
            }
            if(loginIpAdr == ''){
                this.loginIp = true;
            }else{
                this.loginIp = false;
            }
        
            //验证通过，发送登录请求
            if(loginusername !== '' && loginpsd !== '' && loginIpAdr !== ''){
                //封装登录必要数据对象
                var loginObj = {
                    loginName:loginusername,
                    password:loginpsd
                }
                webToast("登陆中...",'middle',60000);
                $.ajax({
                    url:'http://'+ loginIpAdr +':8168/rs/user/login',
                    type: "GET",
                    async: true,
                    data:{
                        loginInfo:JSON.stringify(loginObj)
                    },
                    dataType:"json",
                    contentType:"application/json",
                    error:function(){
                        webToast('连接服务器失败','middle',3000);
                    },
                    success:function(data){
                        console.log('jsondatas',data);
                        if(data.hasOwnProperty("id")){
                            webToast('登陆成功','middle',1000);
                            //绑定用户登录信息，并封装存入本地
                            UserStorage = data;
                            localStorage.setItem('usermassage',JSON.stringify(UserStorage));
                            localStorage.setItem('ServerIp',loginIpAdr);
                            //登录成功，跳转至主页
                            this.which = "loading";
                            setTimeout(function(){
                                window.localStorage.removeItem("checkedTask");
                                location.href='index.html';
                            },1000)
                        }else{
                            webToast(data,'middle',3000);
                        }
                    }
                })
                
            }     
        }
    },
    mounted:function(){
        //获取当前设备ID

        //取得存于本地的用户登录信息
        this.UserStorage = JSON.parse(localStorage.getItem("usermassage"));
        if(this.UserStorage == {} || this.UserStorage == null){
            console.log('no UserStorage',this.UserStorage);
            //当前本地不存在用户信息，进入登录页面
            setTimeout(function(){
                LoginApp.which = 'Login';
            },3000)
        }else{
            console.log('UserStorage',this.UserStorage);
            //当前本地存在用户信息，跳过登录直接进入主页
            setTimeout(function(){
                location.href='index.html';
            },3000)
        }

        //取得存于本地的服务器IP地址
        this.IPstorage = localStorage.getItem("ServerIp");
        if(this.IPstorage == {} || this.IPstorage == null){
            console.log('no IPstorage',this.IPstorage);
            //服务器IP没有获取到
            
        }else{
            console.log('IPstorage',this.IPstorage);
            //当前本地存在用户信息，跳过登录直接进入主页
            $("#ipadress").val(this.IPstorage);
        }
    }
})



