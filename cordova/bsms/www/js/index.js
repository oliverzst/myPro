
var vm = new Vue({
	el: "#index",
	data: function(){
		var checkedTask = JSON.parse(window.localStorage.getItem("checkedTask")) || [];
		return {
// 通用
			appVersion: "0.0.0",
			exitAppTicker: 0,
			isShowFooter: true,
			facilityListRefresh:null,//下拉设备列表的插件对象
			taskListRefresh:null,//下拉任务列表的插件对象
			alarmListRefresh:null,//下拉告警列表的插件对象
			isShowHasNewTask: false,//底部任务图标显示有新任务标记
			isShowHasNewAlarm: false,//底部告警图标显示有新任务标记
			showLoading:false,//是否显示加载动画
			user: {
				userId:1,
				userName:'超级管理员',
				roleId:3,
				roleName:'超级管理员',
				facilityGroupId:0
			},
			passwordModel:{
				oldPassword:'',
				newPassword:'',
				confirmPassword:''
			},
			//封装用户登录信息对象
			UserStorage:{},
			//获取存入本地服务器IP
			IPstorage:'',
		
			title: "设备列表",
			goBackIcon: false,//title栏上是否显示返回按钮
			lastTitle: [],//上个页面的title
			lastContent: [],//上个页面
			lastGoBackIcon: [],//上个页面是否显示返回按钮
			// content:"页面主区域"
			// 取值如下：
			// facility:"设备列表界面"，
			// inspect:设备巡检
			// facilityDetail:"设备详情页面"，
			// facilityAlarm:"设备告警页面"
			// task:"任务列表页面"
			// alarm:"告警列表页面"
			// myself:"我的页面"
			// logManage:"日志管理"
			// permissionConfig:"配置权限"
			// deviceConfig:"配置设备范围"
			// FacilityTypeConfig:"配置设备类型"
			// changeType:"更换设备类型"
			// FacilityTypeManage:"设备类型管理"
			// FacilityTypeMenuManage:"配置维护项目"
			// FacilityTypeModuleManage:"配置设备子模块"
			// adddevicegroup："设备组添加"
			// modifydevicegroup:"修改设备组"
			// FacilityGroupDetails:"设备组包含设备"
			// UserList:"下级用户列表"
			// facilityHistoryItemDetail:设备历史记录项详情板块
			// facilityInspectAddRemarks:设备巡检添加备注板块
			// userMessage:登录用户信息板块
			
			content: "facility",
// 设备列表
			facilitySearchCondition: "",//设备搜索条件
			facilityList:[
			// {//设备列表数据
			// 	address:"",
			// 	domain:"r85@pdt.cn",
			// 	id:1,
			// 	ip:"192.168.1.213",
			// 	lastTask:"",
			// 	lastTaskId:2,
			// 	latitude:30,
			// 	longitude:103,
			// 	manufacturer:"",
			// 	name:"成都基站",
			// 	status:"系统管理员，正在巡检中",
			// 	sysNumber:"",
			// 	alarms:[],
			// 	menuList: [{
			// 		id:1,
			// 		name:"电源",
			// 		inspectResult:{
			// 			preResult: false,
			// 			result: false,
			// 		}
			// 	},
			// 	{
			// 		id:2,
			// 		name:"控制",
			// 		inspectResult:{
			// 			preResult: false,
			// 			result: false,
			// 		}
			// 	},
			// 	{
			// 		id:3,
			// 		name:"天线",
			// 		inspectResult:{
			// 			preResult: false,
			// 			result: false,
			// 		}
			// 	}]
			// }
			],
			currentFacility:{//当前选中的设备
				menuList: []
			},	
			currentFacilityHistory:[],//当前设备的所有历史记录
			currentFacilityHistoryItem:{},//正在查看的当前设备历史记录的那一项
// 设备详情列表
			facilityDetailFilter: "detail",//设备详情过滤：：detail:"详情"，history:"历史"
			facilityAlarmExtendIndex: -1,//展开设备告警项的序号
			// 是否显示设备过滤，查看设备详情时显示，从巡检查看历史时不显示
			isShowFacilityFilter: true,
//设备巡检
			isInspectWithGps: false,
			commitInspectBtnText: "提交",
			isProcessingAfterCommitInspect: false,
			isShowSelectInspectType_base: false,
			currentInspectTaskId: -1,//正执行任务的id，提交用
			isShowInspectBindTask: false,
			isShowPreCommitTaskBtn: true,
			// 当前可以提交的任务
			currentInspectBindTasks: [],
			// 当前选中的可提交的任务
			currentSelectedInspectBindTasks: [],
			isEnabledFacilityInspect: false,
			isShowSelectFaultModule: false,
			inspectDeviceType: [],//巡检设备类型：PDT、电源、网络等
			isContinueInspect: false,
			addInspectItemName: "",
			activeAddInspectItem: false,
			facilityInspect:[//常规巡检
			// {
			// 		id:2,
			// 		name:"控制",
			// 		inspectResult:{
			// 			preResult: false,
			// 			result: false,
			// 		}
			// 	}
			],
			facilityInspectItem:-1,//当前巡检项
			facilityInspectRemarks:{
				name:"总结",
				comment:"",
				comment_temp:""
			},
			facilityInspectRemarksFocus: false,
			signInspectUsable: true,
			commitInspectUsable: false,
			isShowFacilityHistoryItemRemarks: true,
			isShowExtendRemarks: true,
			isShowDelPicBtn: true,
// 故障处理（巡检）
			faults: [
			// {
			// 	name: "电源",
			// 	index: 1,
			// 	description: "",
			// 	handle: "",
			// 	isHandled: false,
			// 	// 交互：是否展开显示
			// 	isExtended: true,
			//  photo: []
			// }
			],
			currentFaultItem: null,
			currentFaultItemFlag: "",
			currentFaultPic: -1,
			bigPicture: "",
// 故障模块
			inspectFaultModule: [],
// 值守任务
			dutys: [{
				content: "",
				place: "",
				records: [{
					content: ""
				}]
			}],
// 发布任务
			isShowSelectInspectType: false,
			currentInspectType: null,
			taskPublish: {
				// 任务名字
				taskName:"",
				// 任务描述
				taskDescription:"",
				//选择设备
				selectedFacilityIndex: [],
				selectedFacilityIndex_temp: [],
				// 选择人员
				selectedPerformerIndex: [],
				selectedPerformerIndex_temp: [],
				// 任务类型：1、巡检任务，2、值守任务，默认为巡检任务
				type: 1,
				// 巡检设备类型
				inspectDeviceTypeId: {
					id: -1,
					name: ""
				},
				inspectTime: ""
			},

// 任务列表
			isShowGettingCurrentPosition: false,
			currentTask: null,//正在执行的任务
			taskList: null,//默认为null，表示与服务器断开，有数据时为数组
			taskList_my:null,//我的任务
			taskList_acceptable:null,//可接任务
			taskList_myRelease:null,//我的发布
			taskFilter: "acceptable",//任务过滤：：acceptable:"所有"，my："我的",publishTask:"发布"
			taskExtendId: -1,//展开详情的任务的id
			checkedTask: checkedTask,//查看过的任务，用于标示新任务
// 告警列表
			alarmList: null,//默认为null，表示与服务器断开，有数据时为数组
			alarmList_all:null,
			alarmList_serious:null,
			alarmList_normal:null,
			alarmList_slight:null,
			alarmFilter: "serious",//告警过滤：：serious:"严重"，normal:"一般"，slight:"轻微"
			currentFacilityAlarm: [],//当前设备的所有告警
//日志管理
			logList:[],    //当前用户的所有历史记录
			logListItem:{},    //正在查看当前用户的哪一项历史记录
			logFilter:"history", //日志过滤 ：：history:"历史记录"，gather:"日志汇总"
//日志列表
			logExtendIndex:-1, //展开日志详情的序号
			currentExportUserId:0, // 进入的用户ID
//日志汇总
			logGather:{
				inspectName:"无",
				partakeTask:0,
				acceptTask:0,
				autoTask:0,
				watch:0,
				completeTime:"无"
			},
//日志统计数据
			totalTypeChartsList:[],
			totalTypeChartsSeries:[],
			totalDeviceChartsList:[],
			totalDeviceChartsSeries:[],
			taskEverydayChartsSeries:[],
			taskEverydayChartsXaxis:[],
			TypeisEmpty:true,
			TypeShowWitch:'none',
			selectUserLists:[],
			allChartsList:[],
			allChartsSeries:[],

			totalTypeChartsList2:[],
			totalTypeChartsSeries2:[],
			totalDeviceChartsList2:[],
			totalDeviceChartsSeries2:[],
			taskEverydayChartsSeries2:[],
			taskEverydayChartsXaxis2:[],
			DeviceShowWitch:'none',
			DeviceisEmpty:true,
			selectDeviceLists:[],
			allChartsList2:[],
			allChartsSeries2:[],

			totalTypeChartsList3:[],
			totalTypeChartsSeries3:[],
			totalDeviceChartsList3:[],
			totalDeviceChartsSeries3:[],
			taskEverydayChartsSeries3:[],
			taskEverydayChartsXaxis3:[],
			MyShowWitch:'none',
			MyisEmpty:true,
			selectMyDeviceLists:[],
			allChartsList3:[],
			allChartsSeries3:[],

			defaultTime:'',
//下级用户列表
			UserList:[],
//设备类型用户列表
			UserFacilityType:[],
			ModifyFacilityTypeList:[],
			setUserFacilityType:{
				FacilityTypeUserId:0,
				setUserFacilityType: [],
				setUserFacilityType_temp: [],
			},
//设备类型列表	
			FacilityTypeLists:[],
//添加设备类型名称
			AddFacilityTypeName:'',
//巡检项目列表
			FacilityTypeMenuLists:[],
//添加巡检项目名称
			AddFacilityTypeMenuName:'',
//添加巡检项目描述
			AddFacilityTypeMenuDes:'',
			FacilityTypeMenuListsParentId:0,

//子模块列表
			FacilityTypeModuleLists:[],
//添加子模块名称
			AddFacilityTypeModuleName:'',
//添加子模块描述
			AddFacilityTypeModuleDes:'',
			FacilityTypeModuleListsParentId:0,
//重置用户密码
			ResetPsdUserList:[],
//配置用户权限
			UserPermission:[],
			permissionCheckbox:false,  //权限配置按钮
//设备组列表 
			deviceGroup:[],         //设备组列表
			deviceGroupDetails:[],  //设备组下的基站列表
			DetailsExtendIndex:-1, //展开日志详情的序号
//设备组用户列表
			UserFacilityGroup:[],
//设备列表
			FacilityList:[],		  //管理设备组的设备列表
			ModifyFacilityList:[],   //修改设备组的设备列表
			changeFacilityList:[],    //配置用户设备组的设备组列表
			//设置用户设备组请求数据
			setFacilityGroup:{
				userId:0,
				facilityGroupId:0
			},
			FacilityGroupName:'',   //添加设备组名称
			ModifyFacilityGroupName:'',   //修改设备组名称
			//添加设备组
			saveFacility: {
				//选择设备
				FacilityGroupId:0,
				saveFacilityDomain: [],
				saveFacilityDomain_temp: [],
			},
			//修改设备组
			setFacility:{
				FacilityGroupId:0,
				setFacilityDomain: [],
				setFacilityDomain_temp: [],
			},
// websocket实例
			alarmSocket: null,
			taskSocket: null,
// URL前缀
			taskUrlPrefix: "",
			logHttp: "",
			userHttp: "",
			roleHttp: "",
			deviceGroupHttp:"",
			deviceHttp:"",
			deviceTypeHttp:"",
			setDeviceTypeHttp:"",
			setDeviceHttp:"",
			setDeviceGroupHttp:"",
			delDeviceGroupHttp:"",
			modifyPsdHttp:"",
			getInspectDeviceType:"",
			getDeviceTypeListHttp:"",
			addFacilityTypeHttp:"",
			delFacilityTypeHttp:"",
			getTypeMenuListHttp:"",
			getTypeModuleListHttp:"",
			addFacilityTypeMenuHttp:"",
			delFacilityTypeMenuHttp:"",
			addFacilityTypeModuleHttp:"",
			delFacilityTypeModuleHttp:"",
			watchHttp:"",
			exportByUserHttp:"",
			getUserChartsHttp:"",
			getDeviceChartsHttp:"",
			getMyChartsHttp:"",
			getHistoryDeviceList:"",
			resetUserPsdHttp:"",
			logoutHttp:"",
		}
	},
	methods:{
		stop: function(e){
			console.log("stop",e)
			// e.stopPropagation();
			// e.preventDefault();
		},
		sureAddInspectItem: function(){
			console.log(vm.addInspectItemName)
			if(vm.addInspectItemName){
				var item = {
					name: vm.addInspectItemName,
					inspectResult:{
						preResult:false,
						remarks:"",
						result:false
					}
				}
				vm.facilityInspect.push(item);	
				vm.activeAddInspectItem = false;			
			}
		},
		removeInspectItem: function(index){
			vm.facilityInspect.splice(index,1);
		},
		//获取数据的统一函数
        getData: function (url, method, param, doneHandler, failHandler) {
            if (url) {
                $.ajax({
                    url: url,
                    type: method || "GET",
                    data: param || "",
                }).done(function (data) {
                    if (doneHandler) {
                        doneHandler(data);
                    }
                }).fail(function (err) {
                    if (failHandler) {
                        failHandler(err);
                    }
                })
            }
        },
        checkTask: function(task){
        	if(vm.taskExtendId==task.id){
        		vm.taskExtendId = -1;
        	}else{
        		vm.taskExtendId=task.id;
        	}
        	if(vm.checkedTask.indexOf(task.id)==-1){
        		vm.checkedTask.push(task.id);
        	}
        	vm.taskList.forEach(function(t){
        		if(t.id == task.id){
        			t.isOld = true;
        		}
        	})
        	var checkedTask_str = JSON.stringify(vm.checkedTask);
        	window.localStorage.setItem("checkedTask",checkedTask_str);

        	if(task.inspectRecords){
        		vm.currentFacilityHistoryItem = task;
        		console.log(task)
        		vm.goToContent("facilityHistoryItemDetail",task.facilityName || task.taskName);
        	}
        },
        checkAlarm: function(alarmIndex){
        	if(vm.facilityAlarmExtendIndex==alarmIndex){
        		vm.facilityAlarmExtendIndex = -1;
        	}else{
        		vm.facilityAlarmExtendIndex=alarmIndex;
        	}
        
        },
        showAcceptableTask: function(){
        	vm.taskFilter='acceptable';
        	vm.taskList = vm.taskList_acceptable;
        },
        showMyTask: function(){
        	vm.taskFilter='my';
        	vm.taskList = vm.taskList_my;
        },
        showMyReleaseTask: function(){
        	vm.taskFilter='myRelease';
        	vm.taskList = vm.taskList_myRelease;
        },
        showAllAlarm: function(){
        	vm.alarmFilter='all';
        	vm.alarmList=vm.alarmList_all;
        },
        showSeriousAlarm: function(){
        	vm.alarmFilter='serious';
        	vm.alarmList=vm.alarmList_serious;
        },
        showNormalAlarm: function(){
        	vm.alarmFilter='normal';
        	vm.alarmList=vm.alarmList_normal;
        },
        showSlightAlarm: function(){
        	vm.alarmFilter='slight';
        	vm.alarmList=vm.alarmList_slight;
        },
        // 接受任务
        acceptTask: function(task){
        	var url = vm.taskUrlPrefix + "/receiveTask";
        	var param = {
        		userId: vm.user.userId,
        		taskId: task.id
        	}
        	vm.getData(url,"GET",param,function(data){
        		console.log(data);
        		if(data=="success"){
        			webToast("接受任务成功",'middle',2000);
        		}
        	},function(err){
        		console.log(err);
        		webToast("接受任务出错",'middle',2000);
        	});
        },
        //任务签到
        signinInspect: function(){
        	if(vm.currentInspectType!=null){//巡检
				var url = vm.taskUrlPrefix + "/signNoTask";
	        	var param = {
	        		userId: vm.user.userId,
	        		facilityDomain: vm.currentFacility.domain,
	        		inspectDeviceType: vm.currentInspectType.id
	        	}
	        	vm.currentInspectTaskId = -1;
	        	vm.getData(url,"GET",param,function(data){
	        		if(data){
	        			// webToast("签到巡检成功","middle",500);
	        			vm.currentInspectTaskId = data;
	        		}
	        	},function(err){console.log(err);webToast("签到巡检出错","middle",1000);});
        	}else if(vm.currentInspectType==null){//值守
				var url = vm.taskUrlPrefix + "/signWatchTask";
	        	var param = {
	        		taskId: vm.currentTask.id
	        	}
	        	vm.getData(url,"GET",param,function(data){
	        		if(data=="success"){
	        			webToast("签到值守成功","middle",500);
	        		}else{
	        			webToast("签到值守失败","middle",1000);
	        		}
	        	},function(err){console.log(err);webToast("签到值守出错","middle",1000);});
        	}
        	
        },
        // 存为草稿
        saveDraft: function(){
        	var param, draftKey;
			if(vm.currentInspectType!=null){          //巡检
				var inspectInfo = [], inspectRemarks = {};
				if(vm.isEnabledFacilityInspect){
					inspectInfo = vm.facilityInspect.map(function(item){
						return item;
					});
					inspectInfo.push(vm.facilityInspectRemarks);
				}
				
				var inspectResult = JSON.stringify(inspectInfo);
				param = {
	        		"userId": vm.user.userId,
	        		"facilityDomain": vm.currentFacility.domain,
	        		"records": inspectResult,
	        		"taskIds": vm.currentSelectedInspectBindTasks.join(","),
	        		"inspectDeviceType": vm.currentInspectType.id,
	        		"troubleShootInfo": (function(){
	        			        			var info = [];
	        			        			vm.faults.forEach(function(fault){
	        			        				var oneInfo = {
	        			        					moduleName: fault.name.name,
	        			        					moduleId: fault.name.id,
	        			        					number: fault.index,
	        			        					description: fault.description,
	        			        					process: fault.handle,
	        			        					isResolve: fault.isHandled?1:0,
	        			        					photo: fault.photo
	        			        				}
	        			        				info.push(oneInfo);
	        			        			})
	        			        			var str = JSON.stringify(info);
	        			        			return str;
	        			        		})()
	        	}
	        	draftKey = "DRAFT" + param.facilityDomain + "DRAFT" +param.inspectDeviceType + "DRAFT" +param.userId;
			}else if(vm.currentInspectType==null){    //值守
				param = {
					taskId: vm.currentTask.id,
					records: JSON.stringify(vm.dutys)
				}
				draftKey = "DRAFT" + vm.user.userId + "DRAFT" + param.taskId;
			}

        	var draft = JSON.stringify(param);
        	window.localStorage.setItem(draftKey, draft);
        	console.log(localStorage.getItem(draftKey));

        	webToast("保存草稿成功","middle",1000)
        },
        // 提交任务成功后的处理，目前只针对提交按钮的样式
        processingAfterCommitInspect: function(){
        	vm.isProcessingAfterCommitInspect = true;
        	var time = 5;
        	vm.commitInspectBtnText = "提交成功，"+time+"秒后可再次提交";
        	var interval = setInterval(function(){
        		if(vm.isProcessingAfterCommitInspect){
        			time -= 1;
	        		vm.commitInspectBtnText = "提交成功，"+time+"秒后可再次提交";
	        		if(time == 0){
	        			vm.isProcessingAfterCommitInspect = false;
	        			vm.commitInspectBtnText = "再次提交";
	        			clearInterval(interval);
	        		}
        		}else{
        			if(vm.isInspectWithGps){
        				vm.commitInspectBtnText = "提交";
        			}else{
        				vm.commitInspectBtnText = "获取定位并提交";
        			}
        			
	        		clearInterval(interval);
        		}        		
        	},1000);
        },
        // 提交巡检
        submitInspect: function(){
        	var inspectInfo = [], inspectRemarks = {};
			if(vm.isEnabledFacilityInspect){
				inspectInfo = vm.facilityInspect.map(function(item){
					return item;
				});
				inspectInfo.push(vm.facilityInspectRemarks);
			}
			
			var inspectResult = JSON.stringify(inspectInfo);
		
			var url = vm.taskUrlPrefix + "/submitInspect";
        	var param = {
        		"inspectId": vm.currentInspectTaskId,
        		"userId": vm.user.userId,
        		"facilityDomain": vm.currentFacility.domain,
        		"records": inspectResult,
        		"taskIds": vm.currentSelectedInspectBindTasks.join(","),
        		"inspectDeviceType": vm.currentInspectType.id,
        		"troubleShootInfo": (function(){
        			        			var info = [];
        			        			vm.faults.forEach(function(fault){
        			        				var oneInfo = {
        			        					moduleName: fault.name.name,
        			        					moduleId: fault.name.id,
        			        					number: fault.index,
        			        					description: fault.description,
        			        					process: fault.handle,
        			        					isResolve: fault.isHandled?1:0,
        			        					photo: fault.photo
        			        				}
        			        				info.push(oneInfo);
        			        			})
        			        			var str = JSON.stringify(info);
        			        			return str;
        			        		})()
        	}
        	$.ajax({
        		url:url,
        		type:"POST",
        		data:param,
        		// contentType: "application/json; charset=UTF-8",
        		success:function(data){
	        		if(data=="success"){
	        			vm.processingAfterCommitInspect();
						webToast("提交成功",'middle',1000);
	        			var draftKey = "DRAFT" + param.facilityDomain + "DRAFT" +param.inspectDeviceType + "DRAFT" +param.userId;
	        			window.localStorage.removeItem(draftKey);
	        			vm.getCurrentFacilityHistory();
	        		}
	        		if(data=="failure"){
	        			webToast("提交失败",'middle',1000);
	        		}
	        	},
        		fail:function(err){
        			console.log(err);
        			webToast("提交失败",'middle',1000);
        		}
        	})
        },
        // 提交值守
        submitWatch: function(){
			var url = vm.taskUrlPrefix + "/submitWatch";
			var param = {
				taskId: vm.currentTask.id,
				records: JSON.stringify(vm.dutys)
			}
			$.ajax({
        		url:url,
        		type:"POST",
        		data:param,
        		success:function(data){
	        		if(data=="success"){
	        			vm.processingAfterCommitInspect();	        			
						webToast("提交成功",'middle',1000);
	        			var draftKey = "DRAFT" + vm.user.userId + "DRAFT" + param.taskId;
	        			window.localStorage.removeItem(draftKey);
	        		}
	        	},
        		fail:function(err){
        			console.log(err);
        			webToast("提交失败",'middle',1000);
        		}
        	})
        },
        //提交任务
		commitInspect: function(){
			if(vm.isInspectWithGps){
				if(vm.currentInspectType!=null){          //巡检
					vm.submitInspect();
				}else if(vm.currentInspectType==null){    //值守
					vm.submitWatch();
				}
			}else{
				vm.getCurrentPosition(
					function(position){
						vm.isShowGettingCurrentPosition = false;
						vm.signBaseStation(position,
							function(){
								vm.isInspectWithGps = true;
								if(vm.currentInspectType!=null){          //巡检
									vm.submitInspect();
								}else if(vm.currentInspectType==null){    //值守
									vm.submitWatch();
								}
			        			webToast("到站签到成功","middle",1000);
							},
							function(){
								webToast("到站签到失败, 无法提交，建议您保存草稿稍后再提交！","middle",1000);
							}
						)			        	
					},
					function(error){
						vm.$alert("获取定位失败，无法提交，建议您保存草稿稍后再提交！")
					}
				)
			}
		},
        // 做任务
        doTask: function(task){
        	if(task.type!=2){
        		vm.getCurrentPosition(
        			function(position){
        				vm.isShowGettingCurrentPosition = false;
			        	vm.signBaseStation(position,
			        		function(){
					        	vm.currentTask = task;
				        		var domain = task.facilityDomain;
					   			vm.facilityList.forEach(function(facility){
					   				if(facility.domain == domain){
					   					vm.currentFacility = facility;
					   				}
					   			})
					   			var inspectType = task.inspectDeviceType;
					   			vm.isInspectWithGps = true;
					   			vm.goToInspect(inspectType,"fromTask");	
			        			webToast("签到成功","middle",500);
			        		},
			        		function(){

			        		}
			        	)
		        		        		
		        	},
		        	function(error){
						var label = "";
			        	switch(error.code){
			        		case error.TIMEOUT:
			        			label = "获取地理位置超时, \n是否先进行巡检，稍后再定位提交？";
			        			break;
			        		case error.PERMISSION_DENIED:
			        			label = "用户拒绝了位置服务, \n是否先进行巡检，稍后再定位提交？";
			        			break;
			        		case error.POSITION_UNAVAILABLE:
			        			label = "定位服务不可用, \n是否先进行巡检，稍后再定位提交？";
			        			break;
			        		default:
			        			label = "获取地理位置失败, \n是否先进行巡检，稍后再定位提交？";
			        	}
						vm.$confirm(label, "提示", {
			        		confirmButtonText: '确定',
		          			cancelButtonText: '取消',
		          			type: 'warning',
		          			center: true
			        	}).then(function(){
			        		vm.currentTask = task;
			        		var domain = task.facilityDomain;
				   			vm.facilityList.forEach(function(facility){
				   				if(facility.domain == domain){
				   					vm.currentFacility = facility;
				   				}
				   			})
				   			var inspectType = task.inspectDeviceType;
				   			vm.isInspectWithGps = false;
				   			vm.goToInspect(inspectType,"fromTask");	
			        	}).catch(function(){})
		        	}
	        	)
        	}else{//值守任务，不定位
        		vm.currentTask = task;
        		vm.isInspectWithGps = true;
		        vm.goToInspect(null,"fromTask");
        	}
        	
        	
        },
        //结束任务
        finishTask: function(task){
			var url = vm.taskUrlPrefix + "/finishTask";
        	var param = {
        		taskIds: ""+task.id
        	}
        	vm.getData(url,"GET",param,function(data){
        		if(data=="success"){
        			webToast("结束任务成功",'middle',1000);
        		}else{
        			webToast("结束任务失败",'middle',1000);
        		}
        	},function(err){
        		console.log(err);
        		webToast("结束任务出错",'middle',1000);
        	});
        },
        //取消任务
        deleteTask: function(task){
			var url = vm.taskUrlPrefix + "/deleteTask";
        	var param = {
        		taskIds: ""+task.id
        	}
        	vm.getData(url,"GET",param,function(data){
        		if(data=="success"){
        			webToast("取消任务成功",'middle',1000);
        		}else{
        			webToast("取消任务失败",'middle',1000);
        		}
        	},function(err){
        		console.log(err);
        		webToast("取消任务出错",'middle',1000);
        	});
        },
        // 获取所有设备列表数据接口
        getFacilityList: function(){
        	var url = vm.deviceHttp;
        	vm.getData(url,"GET",{
        		userId: vm.user.userId
        	},function(data){
        		if(data&&data!="failure"){
        			var temp = data.map(function(facility){
        				facility.firstPY = makePy(facility.name);
        				return facility;
        			})
        			vm.facilityList = temp;
        			vm.extendFacilityWithAlarm();
        		}
        		vm.facilityListRefresh.endDownLoading(true);
        		// webToast("刷新设备列表成功","middle",500)
        	},function(err){
        		console.log(err);
        		vm.facilityListRefresh.endDownLoading(true);
        	})
        },
        // 获取设备历史
        getCurrentFacilityHistory: function(){
        	vm.currentFacilityHistory = [];
			var url = vm.taskUrlPrefix + '/getFacilityTaskHistory';
			var param = {
				facilityDomain : vm.currentFacility.domain
			}
			vm.getData(url,"GET",param,function(data){
				console.log("currentFacilityHistory: ",data);
				vm.currentFacilityHistory = data;				
			},function(err){
				console.log("err:",err)
			})
        },
			
        // 获取所有任务列表数据接口
        getTaskList: function(url) {
        	vm.getData(url,"GET",{
        		userId: vm.user.userId
        	},function(data){
        		if(data){
        			console.log(data);
        		}
        	},function(err){
        		console.log(err);
        	})
		},
		//时间格式转化为时间戳  
		timeChangetype:function(stringTime){  
			var timestamp2 = Date.parse(new Date(stringTime));  
			return timestamp2;  
		},
		//秒数转换标准时间格式
		transtime:function(a) {
			var days = parseInt(a/86400);
			var hour = parseInt((a - (days*86400))/3600);
			var minu = parseInt((a - (days*86400) - (hour*3600))/60);
			var second = parseInt(a - (hour*3600) - (minu*60));
			var daystr = (days == 0) ? '' : days + '天';
			var hourstr = (hour == 0) ? '' : hour + '时';
			var minstr = (minu == 0) ? '' : minu + '分';
			var secstr = second + '秒';

			var timeD = daystr + hourstr + minstr + secstr;
			return timeD;
		},
		//标准时间格式补0操作
		add0:function(dateStr){
			var new_month = (parseInt((dateStr.split('-'))[1])) > 9 ? ((dateStr.split('-'))[1]) : ('0' + (dateStr.split('-'))[1]);
			var new_days = (parseInt((dateStr.split('-'))[2])) > 9 ? ((dateStr.split('-'))[2]) : ('0' + (dateStr.split('-'))[2]);
			var new_dateStr;
			if((dateStr.split('-'))[2] == undefined){
				new_dateStr = ((dateStr.split('-'))[0]) + new_month;
			}else{
				new_dateStr = ((dateStr.split('-'))[0]) + new_month + new_days;
			}
			return new_dateStr;
		},

//配置权限 模块
		//获取权限配置用户列表  （点击：我的 -- 配置权限）
		getPerUserList:function(url){
			vm.UserPermission = [];
			vm.getData(url,"GET",{
        		userId: vm.user.userId
        	},function(data){
        		if(data){
					$.each(data,function(i){
						vm.UserPermission.push({
							userName:data[i].name,
							id:data[i].id,
							roleName:data[i].role.name,
							roleId:data[i].roleId,
							roleIdBoole:(data[i].roleId == 2) ? true : false
						});
					})
					console.log('权限用户列表',vm.UserPermission);
        		}
        	},function(err){
        		console.log(err);
        	})
		},
		//设定用户权限请求接口 （点击：我的 -- 配置权限 -- 保存当前设置）
		setUserRole:function(url){
			var setUserObj = vm.UserPermission;
			$.each(setUserObj,function(i){
				if(setUserObj[i].roleIdBoole == true){
					setUserObj[i].roleId = 2
				}else{
					setUserObj[i].roleId = 3
				}
			})
			console.log('设置权限',setUserObj);
			vm.getData(url,"GET",{
				userLists: JSON.stringify(setUserObj)
			},function(data){
				if(data){
					if(data == '配置成功'){
						webToast(data,'middle',3000);
						setTimeout(function(){
							vm.UserPermission = [];
							vm.getPerUserList(vm.userHttp);
						},2000)
					}else{
						webToast(data,'middle',3000);
					}
				}
			},function(err){
				console.log(err);
			})
		},

//设备组管理 模块
		//获取设备组列表  （点击：我的 -- 设备组管理）
		getDeviceGroup:function(url){
			vm.getData(url,"GET",{},function(data){
				vm.deviceGroup = data;
				console.log("管理设备组的设备组列表",vm.deviceGroup);
			},function(err){
				console.log(err);
			})
		},
	//查看设备组模块   （点击：我的 -- 设备组管理 -- 设备组名称）
		//查看该设备组下所包含的所有设备
		showGroupDetail:function(content){
			console.log('content',content);
			vm.deviceGroupDetails = [];
			vm.deviceGroupDetails = content;
		},
	//修改设备组模块
		//获取修改设备组的当前设备组的基站信息  （点击：我的 -- 设备组管理 -- 修改）
		showCurrentList:function(url,name,id,Facilities){
			if(id == 1){
				webToast('该设备组无法修改','middle',2000);
			}else{
				vm.goToContent('modifydevicegroup','修改设备组');
				vm.setFacility.FacilityGroupId = id;
				vm.ModifyFacilityGroupName = name;
				vm.setFacility.setFacilityDomain_temp = [];
				vm.getData(url,"GET",{
					userId: vm.user.userId
				},function(data){
					vm.ModifyFacilityList = data;
					$.each(Facilities,function(i){
						vm.setFacility.setFacilityDomain_temp.push(Facilities[i].domain)
					});
					console.log("修改设备列表",vm.ModifyFacilityList);
				},function(err){
					console.log(err);
				})
			}
		},
		//修改设备组信息   （点击：我的 -- 设备组管理 -- 修改 -- 保存）
		modifyFacilityGroup:function(url,id){
			vm.setFacility.setFacilityDomain = vm.setFacility.setFacilityDomain_temp;
			console.log(vm.setFacility.setFacilityDomain);
			vm.getData(url,"GET",{
				id:vm.setFacility.FacilityGroupId,
				name:vm.ModifyFacilityGroupName,
				facilityDomains:(vm.setFacility.setFacilityDomain).toString()
			},function(data){
				console.log("修改用户组返回",data);
				if(data == 'success'){
					webToast('修改成功','middle',1500);
					setTimeout(function(){
						vm.goBack();
						vm.getDeviceGroup(vm.deviceGroupHttp);
					},2000)
				}else{
					webToast('修改失败，请重试','middle',1500);
				}
			},function(err){
				console.log(err);
			})
		},
	//添加设备组模块
		//获取添加设备组的 所有设备列表  （点击：我的 -- 设备组管理 -- 添加设备组）
		getDevice:function(url){
			vm.getData(url,"GET",{
				userId: vm.user.userId
			},function(data){
				vm.FacilityList = data;
				console.log("设备列表",vm.FacilityList);
			},function(err){
				console.log(err);
			})
		},
		//添加当前设定的设备为设备组   （点击：我的 -- 设备组管理 -- 添加设备组 -- 保存）
		saveFacilityGroup:function(url){
			//判断组名或设备列表是否为空
			if(vm.FacilityGroupName == '' || vm.saveFacility.saveFacilityDomain_temp == []){
				webToast('设备组名或设备列表不能为空','middle',1500)
			}else{
				vm.saveFacility.saveFacilityDomain = vm.saveFacility.saveFacilityDomain_temp;
				console.log(vm.saveFacility.saveFacilityDomain);
				vm.getData(url,"GET",{
					id:vm.saveFacility.FacilityGroupId,
					name:vm.FacilityGroupName,
					facilityDomains:(vm.saveFacility.saveFacilityDomain).toString()
				},function(data){
					console.log("设置用户组返回",data);
					if(data == 'success'){
						webToast('保存成功','middle',1500);
						setTimeout(function(){
							vm.goBack();
							vm.getDeviceGroup(vm.deviceGroupHttp);
						},2000)
					}else{
						webToast('保存失败，请重试','middle',1500);
					}
				},function(err){
					console.log(err);
				})
			}
		},
	//删除设备组模块
		//删除设备组信息   （点击：我的 -- 设备组管理 -- 删除）
		delFacilityGroup:function(url,id){
			if(id == 1){
				webToast('该设备组无法删除','middle',2000);
			}else(
				
				vm.getData(url,"GET",{
					facilityGroupIds:id,
				},function(data){
					console.log("删除设备组返回",data);
					if(data == 'success'){
						webToast('删除成功','middle',1500);
						setTimeout(function(){
							vm.getDeviceGroup(vm.deviceGroupHttp);
						},2000)
					}else{
						webToast('删除失败，请重试','middle',1500);
					}
				},function(err){
					console.log(err);
				})
			)
		},
//配置用户巡检设备类型 模块
		//获取下级用户列表
		getTypeUserList:function(url){
			vm.getData(url,"GET",{
				userId: vm.user.userId
			},function(data){
				if(data){
					console.log('设备类型用户列表',data);
					vm.UserFacilityType = [];
					$.each(data,function(i){
						vm.UserFacilityType.push({
							userName:data[i].name,
							inspectTypeIds:data[i].inspectDeviceType,
							inspectTypeName:data[i].inspectDeviceTypeName,
							inspectDeviceTypeList:data[i].inspectDeviceTypeList,
							id:data[i].id
						})
					})
				}
			},function(err){
				console.log(err);
			})
		},
		//配置用户设备类型
		getUserDeviceType:function(url,id,facilityTypes){
			vm.setUserFacilityType.FacilityGroupId = id;
			vm.setUserFacilityType.setUserFacilityType_temp = [];
			vm.getData(url,"GET",{},function(data){
				vm.ModifyFacilityTypeList = data;
				$.each(facilityTypes,function(i){
					vm.setUserFacilityType.setUserFacilityType_temp.push(facilityTypes[i].id)
				});
				console.log("修改设备类型列表",vm.ModifyFacilityTypeList);
			},function(err){
				console.log(err);
			})
		},
		//保存用户设备类型   （点击：我的 -- ）
		setUserDeviceType:function(url){
			vm.setUserFacilityType.setUserDeviceType = vm.setUserFacilityType.setUserFacilityType_temp;
			vm.getData(url,"GET",{
				userId:vm.setUserFacilityType.FacilityGroupId,
				inspectDeviceType:(vm.setUserFacilityType.setUserDeviceType).toString()
			},function(data){
				console.log("保存配置用户设备类型返回",data);
				if(data == '配置成功'){
					webToast('配置成功','middle',1500);
					setTimeout(function(){
						vm.goBack();
						vm.getTypeUserList(vm.userHttp);
					},2000)
				}else{
					webToast('修改失败，请重试','middle',1500);
				}
			},function(err){
				console.log(err);
			})
		},

//管理设备类型 模块
		//获取所有设备类型列表
		getDeviceTypeList:function(url){
			vm.getData(url,"GET",{},function(data){
				if(data){
					console.log('巡检设备类型列表',data);
					vm.FacilityTypeLists = [];
					$.each(data,function(i){
						vm.FacilityTypeLists.push({
							id:data[i].id,
							name:data[i].name,
							menuList:data[i].menuList,
							menuName:(function(){
								var menuData = data[i].menuList;
								var menuNameStr='';
								$.each(menuData,function(j){
									menuNameStr += menuData[j].name + ',';
								});
								return menuNameStr;
							})(),
							moduleList:data[i].moduleList,
							moduleName:(function(){
								var moduleData = data[i].moduleList;
								var moduleNameStr='';
								$.each(moduleData,function(j){
									moduleNameStr += moduleData[j].name + ',';
								});
								return moduleNameStr;
							})()
						})
					})
				}
			},function(err){
				console.log(err);
			})
		},
		//添加设备类型接口
		addFacilityType:function(url){
			if(vm.AddFacilityTypeName == '' || vm.AddFacilityTypeName == null){
				webToast('设备类型名称不能为空','middle',1500);
			}else{
				vm.getData(url,"GET",{
					id:null,
					name:vm.AddFacilityTypeName
				},function(data){
					console.log("添加巡检设备类型返回",data);
					if(data == 'success'){
						webToast('添加成功,请配置项目和模块','middle',1500);
						setTimeout(function(){
							vm.getDeviceTypeList(vm.getDeviceTypeListHttp);
							vm.AddFacilityTypeName = '';
						},1500)
					}else{
						webToast('添加失败','middle',1500);
					}
				},function(err){
					console.log(err);
				})
			}
		},
		//删除设备类型接口
		deleteFacilityType:function(url,typeId){
			popTipShow.confirm('提示','是否删除当前设备类型？',['确 定','取 消'],function(e){
				var _this = this;
				if(e.target.className == 'ok'){
					console.log('ok');
					vm.getData(url,"GET",{
						id:typeId
					},function(data){
						console.log("删除巡检设备类型返回",data);
						if(data == 'success'){
							webToast('删除成功','middle',1500);
							setTimeout(function(){
								vm.getDeviceTypeList(vm.getDeviceTypeListHttp);
								_this.hide();
							},1500)
						}else{
							webToast('删除失败','middle',1500);
						}
					},function(err){
						console.log(err);
					})
				}else if(e.target.className == 'cancel'){
					console.log('cancel');
					_this.hide();
				}
			})
			
		},
//管理设备类型下巡检项目 模块
		//获取所有巡检项目列表
		getTypeMenuList:function(url,typeId){
			vm.FacilityTypeMenuLists = [];
			vm.FacilityTypeMenuListsParentId = typeId;
			vm.getData(url,"GET",{
				inspectDeviceTypeId:typeId
			},function(data){
				if(data){
					console.log('巡检项目列表',data);
					vm.FacilityTypeMenuLists = data;
				}
			},function(err){
				console.log(err);
			})
		},
		//添加巡检项目接口
		addFacilityTypeMenu:function(url,typeId){
			if(vm.AddFacilityTypeMenuName == '' || vm.AddFacilityTypeMenuName == null){
				webToast('巡检项目名称不能为空','middle',1500);
			}else{
				vm.getData(url,"GET",{
					menuId:null,
					inspectDeviceTypeId:typeId,
					name:vm.AddFacilityTypeMenuName,
					description:vm.AddFacilityTypeMenuDes
				},function(data){
					console.log("添加巡检项目返回",data);
					if(data == 'success'){
						webToast('添加成功','middle',1500);
						setTimeout(function(){
							vm.getTypeMenuList(vm.getTypeMenuListHttp,typeId);
							vm.AddFacilityTypeMenuName = '';
						},1500)
					}else{
						webToast('添加失败','middle',1500);
					}
				},function(err){
					console.log(err);
				})
			}
		},
		//删除巡检项目接口
		deleteFacilityTypeMenu:function(url,menuId,typeId){
			popTipShow.confirm('提示','是否删除当前巡检项目？',['确 定','取 消'],function(e){
				var _this = this;
				if(e.target.className == 'ok'){
					console.log('ok');
					vm.getData(url,"GET",{
						inspectDeviceTypeId:vm.FacilityTypeMenuListsParentId,
						menuId:menuId
					},function(data){
						console.log("删除巡检项目返回",data);
						if(data == 'success'){
							webToast('删除成功','middle',1500);
							setTimeout(function(){
								vm.getTypeMenuList(vm.getTypeMenuListHttp,typeId);
								_this.hide();
							},1500)
						}else{
							webToast('删除失败','middle',1500);
						}
					},function(err){
						console.log(err);
					})
				}else if(e.target.className == 'cancel'){
					console.log('cancel');
					_this.hide();
				}
			})
			
		},
//管理设备类型下子模块 模块
		//获取所有子模块列表
		getTypeModuleList:function(url,typeId){
			vm.FacilityTypeModuleLists = [];
			vm.FacilityTypeModuleListsParentId = typeId;
			vm.getData(url,"GET",{
				inspectDeviceTypeId:typeId
			},function(data){
				if(data){
					console.log('子模块列表',data);
					vm.FacilityTypeModuleLists = data;
				}
			},function(err){
				console.log(err);
			})
		},
		//添加子模块接口
		addFacilityTypeModule:function(url,typeId){
			if(vm.AddFacilityTypeModuleName == '' || vm.AddFacilityTypeModuleName == null){
				webToast('子模块名称不能为空','middle',1500);
			}else{
				vm.getData(url,"GET",{
					moduleId:null,
					inspectDeviceTypeId:typeId,
					name:vm.AddFacilityTypeModuleName,
					description:vm.AddFacilityTypeModuleDes
				},function(data){
					console.log("添加子模块返回",data);
					if(data == 'success'){
						webToast('添加成功','middle',1500);
						setTimeout(function(){
							vm.getTypeModuleList(vm.getTypeModuleListHttp,typeId);
							vm.AddFacilityTypeModuleName = '';
						},1500)
					}else{
						webToast('添加失败','middle',1500);
					}
				},function(err){
					console.log(err);
				})
			}
		},
		//删除子模块接口
		deleteFacilityTypeModule:function(url,moduleId,typeId){
			popTipShow.confirm('提示','是否删除当前子模块？',['确 定','取 消'],function(e){
				var _this = this;
				if(e.target.className == 'ok'){
					console.log('ok');
					vm.getData(url,"GET",{
						inspectDeviceTypeId:vm.FacilityTypeModuleListsParentId,
						moduleId:moduleId
					},function(data){
						console.log("删除子模块返回",data);
						if(data == 'success'){
							webToast('删除成功','middle',1500);
							setTimeout(function(){
								vm.getTypeModuleList(vm.getTypeModuleListHttp,typeId);
								_this.hide();
							},1500)
						}else{
							webToast('删除失败','middle',1500);
						}
					},function(err){
						console.log(err);
					})
				}else if(e.target.className == 'cancel'){
					console.log('cancel');
					_this.hide();
				}
			})
			
		},
//配置设备组 模块
		//获取配置设备组用户列表  （点击：我的 -- 配置设备组）
		getGroupUserList:function(url){
			vm.getData(url,"GET",{
				userId: vm.user.userId
			},function(data){
				if(data){
					console.log('设备组用户列表',data);
					vm.UserFacilityGroup = [];
					$.each(data,function(i){
						vm.UserFacilityGroup.push({
							userName:data[i].name,
							facilityGroupName:(data[i].facilityGroupId == 0)?'无':data[i].facilityGroup.name,
							id:data[i].id
						})
					})
				}
			},function(err){
				console.log(err);
			})
		},
		//配置设备组获取设备组列表   （点击：我的 -- 配置设备组 -- 更换设备组）
		getSelectDeviceGroup:function(url,userId){
			vm.getData(url,"GET",{},function(data){
				vm.setFacilityGroup.userId = userId;
				vm.changeFacilityList = data;
				console.log("配置设备组的设备组列表",vm.changeFacilityList);
			},function(err){
				console.log(err);
			})
		},
		//保存该用户的新的设备组   （点击：我的 -- 配置设备组 -- 更换设备组 -- 选择）
		setUserDeviceGroup:function(url,facilityId){
			vm.setFacilityGroup.facilityGroupId = facilityId;
			vm.getData(url,"GET",vm.setFacilityGroup,function(data){
				if(data){
					console.log(data);
					if(data == '配置成功'){
						webToast(data,'middle',1500);
						setTimeout(function(){
							vm.UserFacilityGroup = [];
							vm.goBack();
							vm.getGroupUserList(vm.userHttp);
						},2000)
					}else{
						webToast(data,'middle',1500);
					}
				}
			},function(err){
				console.log(err);
			})
		},

//日志管理 模块
	//当用户为管理员或者超级管理员的时候，呈现所有下级用户列表
		//获取下级用户列表数据接口  （点击：我的 -- 日志管理）
		getUserList: function(url) {
			vm.getData(url,"GET",{
				userId: vm.user.userId
			},function(data){
				if(data){
					vm.UserList = data;
					console.log('日志用户列表',vm.UserList);
				}
			},function(err){
				console.log(err);
			})
		},
		// 获取所有日志列表数据接口  （点击：我的 -- 日志管理 -- 日志）
		getLogList: function(url,id) {
			vm.logGather.partakeTask = 0;
			vm.logGather.autoTask = 0;
			vm.logGather.acceptTask = 0;
			vm.logGather.watch = 0;
			vm.logGather.completeTime = '无';
			vm.logGather.inspectName = '无';
			vm.currentFacilityHistory = [];
			vm.currentExportUserId = id;
			console.log(id);
        	vm.getData(url,"GET",{
        		userId: id
        	},function(data){
        		if(data){
					console.log('日志列表',data);
					//计算汇总数据
					if(data.length == 0){
						vm.logList = [];
					}else{
						vm.logList = data;
						vm.currentFacilityHistory = data;
						vm.logGather.partakeTask = data.length;
						vm.logGather.inspectName = data[0].inspectUser.name;
						var duration = 0;
						$.each(data,function(i){
							if(data[i].inspectType == 0){
								vm.logGather.autoTask += 1;
							}else{
								vm.logGather.acceptTask += 1;
							};
							duration += vm.timeChangetype(data[i].endTime) - vm.timeChangetype(data[i].inspectTime);
						})
						vm.logGather.completeTime=vm.transtime(parseInt(duration/((data.length)*1000)));
					}
				};
				vm.getUserWatchHistory(vm.watchHttp,id);
        	},function(err){
        		console.log(err);
        	})
		},
//用户值守历史记录
		getUserWatchHistory:function(url,id){
			vm.getData(url,"GET",{
				userId: id,
				// year:"2017",
				// month:"12",
        	},function(data){
        		if(data){
					console.log('值守列表',data);
					vm.logGather.watch = data.length;
					if(data.length == 0){
						console.log("无值守记录");
					}else{
						$.each(data,function(i){
							vm.logList.push({
								inspectType:10,
								endTime:data[i].endTime,
								id:data[i].id,
								inspectRecords:data[i].watchRecords,
								task:data[i].task,
								inspectUser:data[i].watchUser,
								inspectTime:data[i].watchTime,
								facility:data[i].facility,
							});
							// vm.currentFacilityHistory.push({
							// 	inspectType:2,
							// 	endTime:data[i].endTime,
							// 	id:data[i].id,
							// 	inspectRecords:data[i].watchRecords,
							// 	task:data[i].task,
							// 	inspectUser:data[i].watchUser,
							// 	inspectTime:data[i].watchTime,
							// 	facility:data[i].facility,
							// })
						});
					}
					console.log("合并日志数据",vm.logList);
        		}
        	},function(err){
        		console.log(err);
        	})
		},
//通用转换张斯腾需要的时间格式
		changeToTime:function(start,end){
			var startYear,endYear,startMonth,endMonth,yearStr='',monthStr='';
			if($(start).text() == '开始时间' || $(start).text() == ''){
				webToast('请选择开始时间','middle',1500);
			}else{
				startYear = ($(start).text()).substring(0,4);
				startMonth = ($(start).text()).substring(5,7);
			}
			if($(end).text() == '结束时间' || $(end).text() == ''){
				webToast('请选择结束时间','middle',1500);
			}else{
				endYear = ($(end).text()).substring(0,4);
				endMonth = ($(end).text()).substring(5,7);
			}
			//判断
			var differYear = parseInt(endYear) - parseInt(startYear);
			var differMonth = parseInt(endMonth) - parseInt(startMonth);
			if(differYear < 0){
				console.log("年份差值小于0");
				webToast('结束时间不能小于开始时间','middle',1500);
			}else if(differYear == 0){
				 yearStr = endYear;
				 if(differMonth < 0){
					console.log("月份差值小于0");
					webToast('结束时间不能小于开始时间','middle',1500);
				 }else if(differMonth == 0){
					console.log("月份差值等于0，选取当前月");
					monthStr = endMonth;
				 }else if(differMonth > 0){
					console.log("月份差值大于1");
					for(var i=0;i<(differMonth+1);i++){
						monthStr += (parseInt(startMonth) + i) + ',';
					}
					//去掉最后一个','
					monthStr = monthStr.substring(0,monthStr.length -1);
				 }
			}else if(differYear == 1){
				console.log("年份差值等于1");
				yearStr = startYear + ',' + endYear;
				//遍历前一年的月份string值
				for(var i=0;i<(13-parseInt(startMonth));i++){
					monthStr += (parseInt(startMonth) + i) + ',';
				}
				//去掉最后一个','
				monthStr = monthStr.substring(0,(monthStr.length -1));
				//加上隔年的';'
				monthStr = monthStr + ';';
				//遍历后一年的月份string值
				for(var i=0;i<(parseInt(endMonth));i++){
					monthStr += (1 + i) + ',';
				}
				//去掉最后一个','
				monthStr = monthStr.substring(0,(monthStr.length -1));
			}else if(differYear > 1){
				console.log("年份差值大于1");
				for(var i=0;i<(differYear+1);i++){
					yearStr += (parseInt(startYear) + i) + ',';
				}
				//去掉最后一个','
				yearStr = yearStr.substring(0,(yearStr.length -1));

				var allMonth = '1,2,3,4,5,6,7,8,9,10,11,12;';

				//遍历前一年的月份string值
				for(var i=0;i<(13-parseInt(startMonth));i++){
					monthStr += (parseInt(startMonth) + i) + ',';
				}
				//去掉最后一个','
				monthStr = monthStr.substring(0,(monthStr.length -1));
				//加上隔年的';'
				monthStr = monthStr + ';';

				for(var i=0;i<(differYear);i++){
					monthStr += allMonth;
				}
					
				//遍历后一年的月份string值
				for(var i=0;i<(parseInt(endMonth));i++){
					monthStr += (1 + i) + ',';
				}
				//去掉最后一个','
				monthStr = monthStr.substring(0,(monthStr.length -1));
			}
			var timeObj = {
				yearStr:yearStr,
				monthStr:monthStr
			}
			return timeObj;
		},
//按照用户统计相关方法
		//进入按人员统计界面
		goToUserChartPage:function(url){
			vm.goToContent('UserCharts','按人员统计');
			vm.selectUserfilter(vm.userHttp);

			var nowDate = new Date();
			var nowYear = (nowDate.getFullYear()).toString();
			var nowMonth = (nowDate.getMonth() + 1).toString();
			vm.defaultTime = nowYear + "-" + nowMonth;
		},
		//获取选择用户的列表
		selectUserfilter:function(url){
			vm.getData(url,"GET",{
				userId: vm.user.userId
			},function(data){
				if(data){
					console.log('选择用户列表',data);
					vm.selectUserLists = [];
					vm.selectUserLists.push({
						name:'所有人',
						id:null
					});
					$.each(data,function(i){
						vm.selectUserLists.push({
							name:data[i].name,
							id:data[i].id
						});
					})
					
					$.scrEvent({
						data: (function(){
							var nameArr = [];
							$.each(vm.selectUserLists,function(k){
								nameArr.push((vm.selectUserLists)[k].name);
							})
							return nameArr;
						})(),   //数据
						evEle: '.byUser',            //选择器
						title: '选择用户',            // 标题
						defValue: '所有人',             // 默认值
						afterAction: function (data) {   //  点击确定按钮后,执行的动作
						  $(".byUser").text(data);
						}
					});
				}
			},function(err){
				console.log(err);
			})
		},
		//点击统计获取筛选条件
		getUserChartsData:function(url){
			vm.showLoading = true;
			var getTimeObj = vm.changeToTime('.timeStart','.timeEnd');

			var userNameStr = $(".byUser").text();
			var selectUserId = -1;
			$.each(vm.selectUserLists,function(i){
				if((vm.selectUserLists)[i].name == userNameStr){
					selectUserId = (vm.selectUserLists)[i].id;
				}
			})
			if(selectUserId == -1){
				webToast('未找到当前用户','middle',1500);
			}
			//封装请求data
			var dataObj = {
				userId: selectUserId,
				year:getTimeObj.yearStr,
				month:getTimeObj.monthStr,
			};
			
			vm.getData(url,"GET",dataObj,function(data){
        		if(data){
					console.log('按用户统计数据',data);
					vm.showLoading = false;
					//判断返回数据为单设备数据 还是 全设备数据
					if(data.hasOwnProperty('totalType')){
						vm.TypeShowWitch = 'single';
						if((data.totalType).length == 0){
							vm.TypeisEmpty = true;
						}else{
							vm.TypeisEmpty = false;
							var totalTypeData = data.totalType;
							vm.totalTypeChartsList = [];
							vm.totalTypeChartsSeries = [];
							$.each(totalTypeData,function(i){
								//任务次数分布模拟legend数据(下方列表)
								vm.totalTypeChartsList.push({
									name:totalTypeData[i].name,
									count:totalTypeData[i].count,
									time:vm.transtime(totalTypeData[i].aveTime),
									colorClass:"colorClass"+(i+1),
								});
								//任务次数分布饼图series数据
								vm.totalTypeChartsSeries.push({
									name:totalTypeData[i].name,
									value:totalTypeData[i].count,
								})
							});
							//绘制任务次数分布图
							var myChart1 = echarts.init(document.getElementById('taskCharts'));
							var option1 = {
								title: {
									text: '任务类型分布',
									textStyle:{
										fontSize:14
									},
									x: 'center',
									y: 'center'
								},
								tooltip: {
									trigger: 'item',
									formatter: "{a} <br/>{b}: {c} ({d}%)"
								},
								series: [
									{
										name:'任务次数',
										type:'pie',
										radius: ['45%', '70%'],

										avoidLabelOverlap: false,
										label: {
											normal: {
												show: false,
												position: 'center'
											}
										},
										labelLine: {
											normal: {
												show: false
											}
										},
										data:vm.totalTypeChartsSeries
									}
								]
							};
							myChart1.setOption(option1);
							myChart1.resize();
	
							var totalDeviceData = data.totalDevice;
							vm.totalDeviceChartsList = [];
							vm.totalDeviceChartsSeries = [];
							$.each(totalDeviceData,function(i){
								//设备分布模拟legend数据(下方列表)
								vm.totalDeviceChartsList.push({
									name:totalDeviceData[i].name,
									count:totalDeviceData[i].count,
									time:vm.transtime(totalDeviceData[i].aveTime),
									colorClass:"colorClass"+(i+1),
								});
								//设备分布饼图series数据
								vm.totalDeviceChartsSeries.push({
									name:totalDeviceData[i].name,
									value:totalDeviceData[i].count,
								})
							});
							//绘制设备分布图
							var myChart2 = echarts.init(document.getElementById('deviceCharts'));
							var option2 = {
								title: {
									text: '任务基站分布',
									textStyle:{
										fontSize:14
									},
									x: 'center',
									y: 'center'
								},
								tooltip: {
									trigger: 'item',
									formatter: "{a} <br/>{b}: {c} ({d}%)"
								},
								series: [
									{
										name:'任务关联',
										type:'pie',
										radius: ['45%', '70%'],
										avoidLabelOverlap: false,
										label: {
											normal: {
												show: false,
												position: 'center'
											}
										},
										labelLine: {
											normal: {
												show: false
											}
										},
										data:vm.totalDeviceChartsSeries
									}
								]
							};
							myChart2.setOption(option2);
							myChart2.resize();
							
							var everydayData = data.every;
							vm.taskEverydayChartsSeries = [];
							vm.taskEverydayChartsXaxis = [];
		
							$.each(everydayData,function(i){
								//时间任务次数xAxis数据
								if((vm.add0(everydayData[i].dateKey)).length == 6){
									vm.taskEverydayChartsXaxis.push((vm.add0(everydayData[i].dateKey)).substring(4,6));
								}else if((vm.add0(everydayData[i].dateKey)).length == 8){
									vm.taskEverydayChartsXaxis.push((vm.add0(everydayData[i].dateKey)).substring(6,8));
								}
								
								//时间任务次数series数据
								if(everydayData[i].countList == []){
									vm.taskEverydayChartsSeries.push(0);
								}else{
									var countList = everydayData[i].countList;
									var countdata = 0;
									$.each(countList,function(k){
										countdata += countList[k].count
									})
									vm.taskEverydayChartsSeries.push(countdata);
								}
							});
							console.log("test",vm.taskEverydayChartsSeries)
		
							//绘制时间柱状图
							var myChart3 = echarts.init(document.getElementById('detailCharts'));
							var option3 = {
								tooltip: {
									trigger: 'item',
									formatter: function(param){
		
									}
								},
								xAxis: {
									data: vm.taskEverydayChartsXaxis
								},
								yAxis:{
									show:false
								},
								label:{
									normal:{
										show:true,
										position:'top',
										formatter:function(params){
											if (params.value > 0) {
												return params.value;
											}else{
												return '';
											}
										}
									}
								},
								series: [
									{
										type:'bar',
										itemStyle:{
											normal:{
												color:'rgba(237,108,79,1)'
											}
										},
										data:vm.taskEverydayChartsSeries
									
										// data:vm.taskEverydayChartsSeries
									}
								]
							};
						
							myChart3.setOption(option3);
							myChart3.resize();
						}
					}else{
						vm.TypeShowWitch = 'all';
						if(data.length == 0){
							vm.TypeisEmpty = true;
						}else{
							vm.TypeisEmpty = false;
							//绘制任务次数分布图
							var totalTimes = 0;
							vm.allChartsList = [];
							vm.allChartsSeries = [];
							$.each(data,function(i){
								//任务次数分布模拟legend数据(下方列表)
								vm.allChartsList.push({
									name:data[i].name,
									count:data[i].count,
									time:vm.transtime(data[i].aveTime),
									colorClass:"colorClass"+(i+1),
								});
								//任务次数分布饼图series数据
								vm.allChartsSeries.push({
									name:data[i].name,
									value:data[i].count,
								})
								//计算总次数
								totalTimes += data[i].count;
							});

							var myChart4 = echarts.init(document.getElementById('allCharts'));
							var option4 = {
								title: {
									text: '总任务次数',
									textStyle:{
										fontSize:14
									},
									subtext: totalTimes,
									x: 'center',
									y: 'center'
								},
								tooltip: {
									trigger: 'item',
									formatter: "{a} <br/>{b}: {c} ({d}%)"
								},
								series: [
									{
										name:'任务次数',
										type:'pie',
										radius: ['45%', '70%'],
										avoidLabelOverlap: false,
										label: {
											normal: {
												show: false,
												position: 'center'
											}
										},
										labelLine: {
											normal: {
												show: false
											}
										},
										data:vm.allChartsSeries
									}
								]
							};
							myChart4.setOption(option4);
							myChart4.resize();
						}
						
					}
					
        		}
        	},function(err){
				console.log(err);
				vm.showLoading = false;
        	})

		},
//按照设备统计相关方法
		//进入按设备统计界面
		goToDeviceChartPage:function(url){
			vm.goToContent('DeviceCharts','按设备统计');
			vm.selectDevicefilter(vm.getHistoryDeviceList);

			var nowDate = new Date();
			var nowYear = (nowDate.getFullYear()).toString();
			var nowMonth = (nowDate.getMonth() + 1).toString();
			vm.defaultTime = nowYear + "-" + nowMonth;
		},
		//获取选择设备的列表
		selectDevicefilter:function(url){
			vm.getData(url,"GET",{
				userId: vm.user.userId
			},function(data){
				if(data){
					console.log('选择设备列表',data);

					vm.selectDeviceLists = [];
					vm.selectDeviceLists.push({
						name:'所有设备',
						domain:null
					});
					$.each(data,function(i){
						vm.selectDeviceLists.push({
							name:data[i],
							domain:i
						});
					})
					
					$.scrEvent({
						data: (function(){
							var nameArr = [];
							$.each(vm.selectDeviceLists,function(k){
								nameArr.push((vm.selectDeviceLists)[k].name);
							})
							return nameArr;
						})(),   //数据
						evEle: '.byDevice',            //选择器
						title: '选择设备',            // 标题
						defValue: '所有设备',             // 默认值
						afterAction: function (data) {   //  点击确定按钮后,执行的动作
						  $(".byDevice").text(data);
						}
					});
				}
			},function(err){
				console.log(err);
			})
		},
		//点击统计获取筛选条件
		getDeviceChartsData:function(url){
			vm.showLoading = true;
			var getTimeObj = vm.changeToTime('.timeStart2','.timeEnd2');

			var DeviceNameStr = $(".byDevice").text();
			var selectDeviceDomain = '';
			$.each(vm.selectDeviceLists,function(i){
				if((vm.selectDeviceLists)[i].name == DeviceNameStr){
					selectDeviceDomain = (vm.selectDeviceLists)[i].domain;
				}
			})
			if(selectDeviceDomain == ''){
				webToast('未找到当前设备','middle',1500);
			}
			//封装请求data
			var dataObj = {
				facilityDomain: selectDeviceDomain,
				year:getTimeObj.yearStr,
				month:getTimeObj.monthStr,
			};

			vm.getData(url,"GET",dataObj,function(data){
        		if(data){
					console.log('按设备统计数据',data);
					vm.showLoading = false;
					//判断返回数据为单设备数据 还是 全设备数据
					if(data.hasOwnProperty('totalType')){     //单个设备
						console.log('进入单个设备统计');
						vm.DeviceShowWitch = 'single';
						if((data.totalType).length == 0){
							vm.DeviceisEmpty = true;
						}else{
							vm.DeviceisEmpty = false;
							var totalTypeData = data.totalType;
							var totalTimes = 0;
							vm.totalTypeChartsList2 = [];
							vm.totalTypeChartsSeries2 = [];
							$.each(totalTypeData,function(i){
								//任务次数分布模拟legend数据(下方列表)
								vm.totalTypeChartsList2.push({
									name:totalTypeData[i].name,
									count:totalTypeData[i].count,
									time:vm.transtime(totalTypeData[i].aveTime),
									colorClass:"colorClass"+(i+1),
								});
								//任务次数分布饼图series数据
								vm.totalTypeChartsSeries2.push({
									name:totalTypeData[i].name,
									value:totalTypeData[i].count,
								})
								//计算总次数
								totalTimes += totalTypeData[i].count;
							});
	
							//绘制任务次数分布图
							var myChart5 = echarts.init(document.getElementById('taskCharts2'));
							var option5 = {
								title: {
									text: '任务类型分布',
									textStyle:{
										fontSize:14
									},
									x: 'center',
									y: 'center'
								},
								tooltip: {
									trigger: 'item',
									formatter: "{a} <br/>{b}: {c} ({d}%)"
								},
								series: [
									{
										name:'任务次数',
										type:'pie',
										radius: ['45%', '70%'],
										avoidLabelOverlap: false,
										label: {
											normal: {
												show: false,
												position: 'center'
											}
										},
										labelLine: {
											normal: {
												show: false
											}
										},
										data:vm.totalTypeChartsSeries2
									}
								]
							};
							myChart5.setOption(option5);
							myChart5.resize();
	
							var totalDeviceData = data.totalDevice;
							vm.totalDeviceChartsList2 = [];
							vm.totalDeviceChartsSeries2 = [];
							$.each(totalDeviceData,function(i){
								//设备分布模拟legend数据(下方列表)
								vm.totalDeviceChartsList2.push({
									name:totalDeviceData[i].name,
									count:totalDeviceData[i].count,
									time:vm.transtime(totalDeviceData[i].aveTime),
									colorClass:"colorClass"+(i+1),
								});
								//设备分布饼图series数据
								vm.totalDeviceChartsSeries2.push({
									name:totalDeviceData[i].name,
									value:totalDeviceData[i].count,
								})
							});
							//绘制设备分布图
							var myChart6 = echarts.init(document.getElementById('deviceCharts2'));
							var option6 = {
								title: {
									text: '任务人员分布',
									textStyle:{
										fontSize:14
									},
									x: 'center',
									y: 'center'
								},
								tooltip: {
									trigger: 'item',
									formatter: "{a} <br/>{b}: {c} ({d}%)"
								},
								series: [
									{
										name:'任务关联',
										type:'pie',
										radius: ['45%', '70%'],
										avoidLabelOverlap: false,
										label: {
											normal: {
												show: false,
												position: 'inside'
											}
										},
										labelLine: {
											normal: {
												show: false
											}
										},
										data:vm.totalDeviceChartsSeries2,
									}
								]
							};
							myChart6.setOption(option6);
							myChart6.resize();
	
							var everydayData = data.every;
							vm.taskEverydayChartsSeries2 = [];
							vm.taskEverydayChartsXaxis2 = [];
		
							$.each(everydayData,function(i){
								//时间任务次数xAxis数据
								if((vm.add0(everydayData[i].dateKey)).length == 6){
									vm.taskEverydayChartsXaxis2.push((vm.add0(everydayData[i].dateKey)).substring(4,6));
								}else if((vm.add0(everydayData[i].dateKey)).length == 8){
									vm.taskEverydayChartsXaxis2.push((vm.add0(everydayData[i].dateKey)).substring(6,8));
								}
								//时间任务次数series数据
								if(everydayData[i].countList == []){
									vm.taskEverydayChartsSeries.push(0);
								}else{
									var countList = everydayData[i].countList;
									var countdata = 0;
									$.each(countList,function(k){
										countdata += countList[k].count
									})
									vm.taskEverydayChartsSeries2.push(countdata);
								}
							});
							console.log("test",vm.taskEverydayChartsSeries2)
		
							//绘制时间柱状图
							var myChart7 = echarts.init(document.getElementById('detailCharts2'));
							var option7 = {
								tooltip: {
									trigger: 'item',
									formatter: function(param){
		
									}
								},
								xAxis: {
									data: vm.taskEverydayChartsXaxis2
								},
								yAxis:{
									show:false
								},
								label:{
									normal:{
										show:true,
										position:'top',
										formatter:function(params){
											if (params.value > 0) {
												return params.value;
											}else{
												return '';
											}
										}
									}
								},
								series: [
									{
										type:'bar',
										itemStyle:{
											normal:{
												color:'rgba(237,108,79,1)'
											}
										},
										data:vm.taskEverydayChartsSeries2
									}
								]
							};
							myChart7.setOption(option7);
							myChart7.resize();
						}
					}else{										//全部设备
						console.log('进入所有设备统计');
						vm.DeviceShowWitch = 'all';
						if(data.length == 0){
							vm.DeviceisEmpty = true;
						}else{
							vm.DeviceisEmpty = false;
							//绘制任务次数分布图
							var totalTimes = 0;
							vm.allChartsList2 = [];
							vm.allChartsSeries2 = [];
							$.each(data,function(i){
								//任务次数分布模拟legend数据(下方列表)
								vm.allChartsList2.push({
									name:data[i].name,
									count:data[i].count,
									time:vm.transtime(data[i].aveTime),
									colorClass:"colorClass"+(i+1),
								});
								//任务次数分布饼图series数据
								vm.allChartsSeries2.push({
									name:data[i].name,
									value:data[i].count,
								})
								//计算总次数
								totalTimes += data[i].count;
							});

							var myChart8 = echarts.init(document.getElementById('allCharts2'));
							var option8 = {
								title: {
									text: '总任务次数',
									textStyle:{
										fontSize:14
									},
									subtext: totalTimes,
									x: 'center',
									y: 'center'
								},
								tooltip: {
									trigger: 'item',
									formatter: "{a} <br/>{b}: {c} ({d}%)"
								},
								series: [
									{
										name:'任务次数',
										type:'pie',
										radius: ['45%', '70%'],
										avoidLabelOverlap: false,
										label: {
											normal: {
												show: false,
												position: 'center'
											}
										},
										labelLine: {
											normal: {
												show: false
											}
										},
										data:vm.allChartsSeries2
									}
								]
							};
							myChart8.setOption(option8);
							myChart8.resize();
						}
					}
        		}
        	},function(err){
				console.log(err);
				vm.showLoading = false;
        	})

		},
//普通用户我的统计相关方法
		//进入我的统计界面
		goToMyChartPage:function(url){
			vm.goToContent('mystatChart','我的统计');
			vm.selectMyfilter(vm.getHistoryDeviceList);

			var nowDate = new Date();
			var nowYear = (nowDate.getFullYear()).toString();
			var nowMonth = (nowDate.getMonth() + 1).toString();
			vm.defaultTime = nowYear + "-" + nowMonth;
		},
		//获取选择用户的列表
		selectMyfilter:function(url){
			vm.getData(url,"GET",{
				userId: vm.user.userId
			},function(data){
				if(data){
					console.log('选择设备列表',data);
					
					vm.selectMyDeviceLists = [];
					vm.selectMyDeviceLists.push({
						name:'所有设备',
						domain:null
					});
					$.each(data,function(i){
						vm.selectMyDeviceLists.push({
							name:data[i],
							domain:i
						});
					})
					
					$.scrEvent({
						data: (function(){
							var nameArr = [];
							$.each(vm.selectMyDeviceLists,function(k){
								nameArr.push((vm.selectMyDeviceLists)[k].name);
							})
							return nameArr;
						})(),   //数据
						evEle: '.myDevice',            //选择器
						title: '选择设备',            // 标题
						defValue: '所有设备',             // 默认值
						afterAction: function (data) {   //  点击确定按钮后,执行的动作
							$(".myDevice").text(data);
						}
					});
				}
			},function(err){
				console.log(err);
			})
		},
		//点击统计获取筛选条件
		getMyChartsData:function(url){
			vm.showLoading = true;
			var getTimeObj = vm.changeToTime('.timeStart3','.timeEnd3');

			var DeviceNameStr = $(".myDevice").text();
			var selectMyDeviceDomain = '';
			$.each(vm.selectMyDeviceLists,function(i){
				if((vm.selectMyDeviceLists)[i].name == DeviceNameStr){
					selectMyDeviceDomain = (vm.selectMyDeviceLists)[i].domain;
				}
			})
			if(selectMyDeviceDomain == ''){
				webToast('未找到当前设备','middle',1500);
			}
			//封装请求data
			var dataObj = {
				userId:vm.user.userId,
				facilityDomain: selectMyDeviceDomain,
				year:getTimeObj.yearStr,
				month:getTimeObj.monthStr,
			};

			vm.getData(url,"GET",dataObj,function(data){
        		if(data){
					console.log('我的统计数据',data);
					vm.showLoading = false;
					//判断返回数据为单设备数据 还是 全设备数据
					if(data.hasOwnProperty('totalType')){
						vm.MyShowWitch = 'single';
						if((data.totalType).length == 0){
							vm.MyisEmpty = true;
						}else{
							vm.MyisEmpty = false;
							var totalTypeData = data.totalType;
							vm.totalTypeChartsList3 = [];
							vm.totalTypeChartsSeries3 = [];
							$.each(totalTypeData,function(i){
								//任务次数分布模拟legend数据(下方列表)
								vm.totalTypeChartsList3.push({
									name:totalTypeData[i].name,
									count:totalTypeData[i].count,
									time:vm.transtime(totalTypeData[i].aveTime),
									colorClass:"colorClass"+(i+1),
								});
								//任务次数分布饼图series数据
								vm.totalTypeChartsSeries3.push({
									name:totalTypeData[i].name,
									value:totalTypeData[i].count,
								})
							});
							//绘制任务次数分布图
							var myChart9 = echarts.init(document.getElementById('taskCharts3'));
							var option9 = {
								title: {
									text: '任务类型分布',
									textStyle:{
										fontSize:14
									},
									x: 'center',
									y: 'center'
								},
								tooltip: {
									trigger: 'item',
									formatter: "{a} <br/>{b}: {c} ({d}%)"
								},
								series: [
									{
										name:'任务次数',
										type:'pie',
										radius: ['45%', '70%'],

										avoidLabelOverlap: false,
										label: {
											normal: {
												show: false,
												position: 'center'
											}
										},
										labelLine: {
											normal: {
												show: false
											}
										},
										data:vm.totalTypeChartsSeries3
									}
								]
							};
							myChart9.setOption(option9);
							myChart9.resize();
							
							var everydayData = data.every;
							vm.taskEverydayChartsSeries3 = [];
							vm.taskEverydayChartsXaxis3 = [];
		
							$.each(everydayData,function(i){
								//时间任务次数xAxis数据
								if((vm.add0(everydayData[i].dateKey)).length == 6){
									vm.taskEverydayChartsXaxis3.push((vm.add0(everydayData[i].dateKey)).substring(4,6));
								}else if((vm.add0(everydayData[i].dateKey)).length == 8){
									vm.taskEverydayChartsXaxis3.push((vm.add0(everydayData[i].dateKey)).substring(6,8));
								}
								//时间任务次数series数据
								if(everydayData[i].countList == []){
									vm.taskEverydayChartsSeries3.push(0);
								}else{
									var countList = everydayData[i].countList;
									var countdata = 0;
									$.each(countList,function(k){
										countdata += countList[k].count
									})
									vm.taskEverydayChartsSeries3.push(countdata);
								}
							});
							console.log("test",vm.taskEverydayChartsSeries3)
		
							//绘制时间柱状图
							var myChart10 = echarts.init(document.getElementById('detailCharts3'));
							var option10 = {
								tooltip: {
									trigger: 'item',
									formatter: function(param){
		
									}
								},
								xAxis: {
									data: vm.taskEverydayChartsXaxis3
								},
								yAxis:{
									show:false
								},
								label:{
									normal:{
										show:true,
										position:'top',
										formatter:function(params){
											if (params.value > 0) {
												return params.value;
											}else{
												return '';
											}
										}
									}
								},
								series: [
									{
										type:'bar',
										itemStyle:{
											normal:{
												color:'rgba(237,108,79,1)'
											}
										},
										data:vm.taskEverydayChartsSeries3
									
										// data:vm.taskEverydayChartsSeries
									}
								]
							};
							myChart10.setOption(option10);
							myChart10.resize();
						}
					}else{
						vm.MyShowWitch = 'all';
						if(data.length == 0){
							vm.MyisEmpty = true;
						}else{
							vm.MyisEmpty = false;
							//绘制任务次数分布图
							var totalTimes = 0;
							vm.allChartsList3 = [];
							vm.allChartsSeries3 = [];
							$.each(data,function(i){
								//任务次数分布模拟legend数据(下方列表)
								vm.allChartsList3.push({
									name:data[i].name,
									count:data[i].count,
									time:vm.transtime(data[i].aveTime),
									colorClass:"colorClass"+(i+1),
								});
								//任务次数分布饼图series数据
								vm.allChartsSeries3.push({
									name:data[i].name,
									value:data[i].count,
								})
								//计算总次数
								totalTimes += data[i].count;
							});

							var myChart11 = echarts.init(document.getElementById('allCharts3'));
							var option11 = {
								title: {
									text: '总任务次数',
									textStyle:{
										fontSize:14
									},
									subtext: totalTimes,
									x: 'center',
									y: 'center'
								},
								tooltip: {
									trigger: 'item',
									formatter: "{a} <br/>{b}: {c} ({d}%)"
								},
								series: [
									{
										name:'任务次数',
										type:'pie',
										radius: ['45%', '70%'],
										avoidLabelOverlap: false,
										label: {
											normal: {
												show: false,
												position: 'center'
											}
										},
										labelLine: {
											normal: {
												show: false
											}
										},
										data:vm.allChartsSeries3
									}
								]
							};
							myChart11.setOption(option11);
							myChart11.resize();
						}
						
					}
					
        		}
        	},function(err){
				console.log(err);
				vm.showLoading = false;
        	})

		},

//退出登录 模块
		UserExit:function(url){
			popTipShow.confirm('提示',"是否退出？",['确 定','取 消'],function(e){
				var _this = this;
				if(e.target.className == 'ok'){
					vm.getData(url,"GET",{
						userId:vm.user.userId,
					},function(data){
						if(data){
							console.log('退出登录返回',data);
							if(data == '登出成功'){
								webToast(data,'middle',1000);
								localStorage.removeItem("usermassage");
								vm.UserStorage = {};
								console.log("用户信息",vm.UserStorage);
								setTimeout(function(){
									location.href='Login.html';
								},1000)
							}else {
								webToast(data,'middle',2000);
							}
							
						}
					},function(err){
						console.log(err);
						webToast('网络错误','middle',2000);
					})
				}else if(e.target.className == 'cancel'){
					_this.hide();
				}
			})
		},
//修改密码 模块
		modifyPsd:function(url){
			console.log('edd',vm.passwordModel);
			if(vm.passwordModel.newPassword == vm.passwordModel.confirmPassword){
				vm.getData(url,"GET",{
					userId: vm.user.userId,
					oldPassword:vm.passwordModel.oldPassword,
					newPassword:vm.passwordModel.newPassword
				},function(data){
					if(data){
						console.log('修改密码返回',data);
						webToast(data,'middle',1500);
						setTimeout(function(){
							vm.UserExit();
						},1500)
					}
				},function(err){
					console.log(err);
					webToast('网络错误','middle',2000);
				})
			}else{
				webToast('新密码不一致','middle',2000);
			}
		},
//重置密码 模块
		//获取重置密码用户列表
		getResetPsdUserList:function(url){
			vm.ResetPsdUserList = [];
			vm.getData(url,"GET",{
        		userId: vm.user.userId
        	},function(data){
        		if(data){
					$.each(data,function(i){
						vm.ResetPsdUserList.push({
							userName:data[i].name,
							id:data[i].id
						});
					})
					console.log('密码重置用户列表',vm.ResetPsdUserList);
        		}
        	},function(err){
        		console.log(err);
        	})
		},
		//重置密码
		resetUserPsd:function(url,id,name){
			popTipShow.confirm('提示',"是否重置'"+name+"'的密码？</br>重置后密码统一为'12345678'",['确 定','取 消'],function(e){
				var _this = this;
				if(e.target.className == 'ok'){
					console.log('ok');
					vm.getData(url,"GET",{
						userId: id
					},function(data){
						if(data){
							console.log('密码重置返回',data);
							webToast(data,'middle',1500);
							setTimeout(function(){
								vm.getResetPsdUserList(vm.userHttp);
								_this.hide();
							},1500)
						}
					},function(err){
						webToast('重置失败','middle',1500);
					})
				}else if(e.target.className == 'cancel'){
					console.log('cancel');
					_this.hide();
				}
			})
		},

        sureSelectFacility: function(){
        	vm.taskPublish.selectedFacilityIndex = vm.taskPublish.selectedFacilityIndex_temp;
        	vm.goBack();
        },
        sureSelectPerformer: function(){
        	vm.taskPublish.selectedPerformerIndex = vm.taskPublish.selectedPerformerIndex_temp;
        	vm.goBack();
        },
        publishTask: function(){
        	var taskPublish = vm.taskPublish;
        	var url = vm.taskUrlPrefix + "/addTasks";
        	var d = {
        			taskName: taskPublish.taskName,		//任务名称 M
				    description: taskPublish.taskDescription,		//任务描述 M
				    facilityDomains: taskPublish.selectedFacilityIndex.join(","),		//设备IDs  M
				    userId: vm.user.userId,		//发布者ID  M
				    inspectTime: vm.stringifyTime(taskPublish.inspectTime),		//指定任务执行时间O
				    inspectBy: taskPublish.selectedPerformerIndex.join(","),		//指定任务执行人IDs  O
				    type: taskPublish.type,
				    inspectDeviceTypeId: taskPublish.inspectDeviceTypeId.id
        		}
        	var param = {
        		taskInfo:JSON.stringify(d)
        	}
        	vm.getData(url,"GET",param,function(data){
        		console.log(data);
        		if(data=="success"){        			
        			webToast("发布任务成功",'middle',1000);
        		}
        		if(data=="exist"){
        			webToast("任务已存在",'middle',1000);
        		}
        		if(data=="failure"){
        			webToast("发布任务失败",'middle',1000);
        		}
        	},function(err){
        		console.log(err);
        		webToast("发布任务出错",'middle',1000);
        	});
        },
        sureAddFacilityInspectRemarks: function(){
        	var name = vm.facilityInspectItem;
        	if(name == -1){
        		vm.facilityInspectRemarks.comment = vm.facilityInspectRemarks.comment_temp;
        	}else{
	        	vm.facilityInspect.forEach(function(item){
	        		if(item.name == name){
	        			item.inspectResult.remarks = vm.facilityInspectRemarks.comment_temp;
	        		}
        		})
        	} 
        	vm.facilityInspectRemarksFocus = false;
        	vm.goBack();
        },
		goToContent: function(content,title,callback){
			vm.lastTitle.push(vm.title);
			vm.title = title;
			vm.lastContent.push(vm.content);
			vm.content = content;
			vm.lastGoBackIcon.push(vm.goBackIcon);
			vm.goBackIcon = true;
			if(callback && typeof callback==="function"){
				callback();
			}
		},
		// 发布任务里选择设备
		goToSelectFacility: function(){
			vm.goToContent('selectFacility','选择设备',function(){
				vm.taskPublish.selectedFacilityIndex_temp = vm.taskPublish.selectedFacilityIndex;
			})
		},
		// 发布任务里选择设备
		goToSelectPerformer: function(){
			vm.goToContent('selectPerformer','选择执行人',function(){
				vm.getUserList(vm.userHttp);
				vm.taskPublish.selectedPerformerIndex_temp = vm.taskPublish.selectedPerformerIndex;
			})
		},
		// 显示设备详情
		goToFacilityDetail: function(facility){
			vm.currentFacility = facility;
			
			// 获取设备历史
			vm.getCurrentFacilityHistory();
			// 获取巡检设备类型
			vm.getUserInspectDeviceType();

			vm.goToContent('facilityDetail',vm.currentFacility.name,function(){
				vm.facilityDetailFilter = "detail";
				vm.isShowSelectInspectType_base = false;
				vm.isInspectWithGps = false;
			});
		},
		showFacilityHistory: function(){
			vm.goToContent("facilityHistory",vm.currentFacility.name);
		},
		// 添加常规巡检备注
		goToAddFacilityInspectRemarks: function(facilityInspectItem){{
			if(facilityInspectItem){
				vm.facilityInspectItem = facilityInspectItem.name;
				vm.facilityInspectRemarks.comment_temp = facilityInspectItem.inspectResult.remarks;
				vm.facilityInspectRemarksFocus = true;
			}else{
				vm.facilityInspectItem = -1;					
				vm.facilityInspectRemarks.comment_temp = vm.facilityInspectRemarks.comment;
				vm.facilityInspectRemarksFocus = true;
			}}
			if(vm.facilityInspectItem == -1){
				var title = "备注";
			}else{
				var title = vm.facilityInspectItem;
			}
			vm.goToContent("facilityInspectAddRemarks",title);
		},
		// 添加故障巡检内容
		goToAddFacilityInspectFaultRecord: function(faultItem,flag){
			vm.currentFaultItem = faultItem;
			vm.currentFaultItemFlag = flag;
			if(flag=="故障现象"){
				vm.facilityInspectRemarks.comment_temp = faultItem.description;
				var title = faultItem.name.name+" 故障现象";
			}
			if(flag=="处理过程"){
				vm.facilityInspectRemarks.comment_temp = faultItem.handle;
				var title = faultItem.name.name+" 处理过程";
			}
			vm.goToContent("facilityInspectAddFaultRecord",title);
		},
		sureAddFacilityInspectFaultRecord: function(){
			if(vm.currentFaultItemFlag == "故障现象"){
				vm.currentFaultItem.description = vm.facilityInspectRemarks.comment_temp;
			}
			if(vm.currentFaultItemFlag == "处理过程"){
				vm.currentFaultItem.handle = vm.facilityInspectRemarks.comment_temp;
			}
			vm.goBack();
		},
		addFaultItem: function(){
			var fault= {
				name: {
					id: -1,
					name: ""
				},
				index: 1,
				description: "",
				handle: "",
				isHandled: false,
				isExtended: true,
				isShowRemoveBtn: false,
				photo: []
			}
			vm.faults.push(fault);
		},
		addDutyRecord: function(duty){
			var r = {
				content: ""
			};
			duty.records.push(r)

		},
		// 获取用户巡检设备类型
		getUserInspectDeviceType: function(){
			var url = "http://" + vm.IPstorage + ":8168/rs/user/getUserInspectDeviceType";
			var param = {
				userId: vm.user.userId,
			}
			vm.getData(url,"GET",param,function(data){
				console.log(data)
				vm.inspectDeviceType = data;
			},function(err){
				console.log(err)
			})
		},
		// 获取单个巡检设备类型的详情
		getInspectDeviceTypeDetail: function(){
			var url = "http://" + vm.IPstorage + ":8168/rs/inspectDeviceType/getInspectDeviceType";
			var param = {
				id: vm.currentInspectType.id,
			}
			vm.facilityInspect = [];
			vm.inspectFaultModule = [];
			vm.getData(url,"GET",param,function(data){
				console.log(data);
				var menuList = data.menuList || []; 
				var moduleList = data.moduleList || [];
				vm.facilityInspect.splice(0, vm.facilityInspect.length);
				menuList.forEach(function(menu){
					menu["inspectResult"] = {
						preResult: false,
						result: false
					}
					vm.facilityInspect.push(menu);
				})
				vm.inspectFaultModule = moduleList;
			},function(err){
				console.log(err)
			}) 
		},
		//获取用户当前巡检基站可提交的任务列表
		getSubmitTasks: function(){
			var url = "http://" + vm.IPstorage + ":8168/rs/task/getSubmitTasks";
			var param = {
				userId: vm.user.userId,
				facilityDomain: vm.currentFacility.domain,
				inspectDeviceTypeId: vm.currentInspectType.id
			}
			vm.getData(url,"GET",param,function(data){
				console.log(data);
				if(data && data!="failure"){
					vm.currentInspectBindTasks = data;
				}else{
					vm.currentInspectBindTasks = [];
				}
				vm.isShowPreCommitTaskBtn = true;
				vm.currentSelectedInspectBindTasks = [];				
			},function(err){
				console.log(err)
			})
		},
		removeFaultItem: function(faultItemIndex){
			vm.faults.splice(faultItemIndex, 1);
		},
		showSelectFaultModule: function(faultItem){
			if(vm.inspectFaultModule.length){
				vm.isShowSelectFaultModule=true;
				vm.currentFaultItem = faultItem;
			}else{
				webToast("故障模块为空","middle",1000);
			}
			
		},
		showSelectDeviceType: function(){
			if(vm.inspectDeviceType.length){
				vm.isShowSelectInspectType = true;
			}else{
				webToast("巡检类型为空","middle",1000);
			}
		},
		selectFaultModule: function(faultModule){
			vm.isShowSelectFaultModule = false;
			vm.currentFaultItem.name = faultModule;
		},
		selectInspectType: function(inspectType){
			vm.isShowSelectInspectType = false;
			vm.taskPublish.inspectDeviceTypeId = inspectType;
		},
		goToInspect: function(deviceType,from){
			// deviceType: 巡检的设备类型,为null时表示是值守任务
			if(deviceType!==null){
				vm.currentInspectType = deviceType;
				
				var draftKey = "DRAFT" + vm.currentFacility.domain + "DRAFT" +vm.currentInspectType.id + "DRAFT" +vm.user.userId;				
				var draft = window.localStorage.getItem(draftKey);
				if(draft){
					var o = JSON.parse(draft);
					console.log(o)
					var records = JSON.parse(o.records);
					vm.facilityInspect.splice(0, vm.facilityInspect.length);
					vm.isEnabledFacilityInspect = true;
					if(records.length){
						records.forEach(function(record){
							if(record.inspectResult){
								vm.facilityInspect.push(record);
							}else{
								vm.facilityInspectRemarks.comment = record.comment;
								vm.facilityInspectRemarks.comment_temp = record.comment_temp;
							}
						})
					}
					var troubleShootInfo = JSON.parse(o.troubleShootInfo);
					vm.faults = [];
					if(troubleShootInfo.length){
						troubleShootInfo.forEach(function(fault){
							var f = {
								name: {
									id: fault.moduleid,
									name: fault.moduleName
								},
								index: fault.number,
								description: fault.description,
								handle: fault.process,
								isHandled: fault.isResolve,
								isExtended: true,
								isShowRemoveBtn: false,
								photo: fault.photo
							}
							vm.faults.push(f);
						})
					}
				}else{
					vm.isEnabledFacilityInspect = true;
					vm.facilityInspectRemarks.comment = "";
					//获取巡检设备类型详情
					vm.getInspectDeviceTypeDetail();
					//获取可提交的任务
					vm.getSubmitTasks();
					// 初始化故障
					vm.faults = [];
				}

				var title = vm.currentFacility.name + " " + deviceType.name;
			}else{
				var draftKey = "DRAFT" + vm.user.userId + "DRAFT" + vm.currentTask.id;
				var draft = window.localStorage.getItem(draftKey);
				if(draft){
					var o = JSON.parse(draft);
					var records = JSON.parse(o.records);
					vm.dutys = [];
					records.forEach(function(record){
						var duty = {
							content: record.content,
							place: record.place,
							records: record.records
						}
						vm.dutys.push(duty);
					})
				}else{
					vm.dutys = [{
						content: "",
						place: "",
						records: [{
							content: ""
						}]
					}];
				}
				vm.currentInspectType = null;
				vm.currentInspectBindTasks = [];
				var title = "值守任务";
			}
			
        	// 从任务进入巡检，获取设备历史
        	if(from){
        		vm.getCurrentFacilityHistory();
        	}

			vm.goToContent('inspect',title,function(){
				//开始任务签到
				vm.signinInspect();
				vm.isShowInspectBindTask = false;
				vm.isShowPreCommitTaskBtn = true;
				vm.isProcessingAfterCommitInspect = false;
				if(vm.isInspectWithGps){
					vm.commitInspectBtnText = "提交";
				}else{
					vm.commitInspectBtnText = "获取定位并提交";
				}			
			})	
		},
		goToFacilityHistoryItem: function(facilityHistoryItem,thisType){
			var id = facilityHistoryItem.id;
			if(facilityHistoryItem.inspectType !== 10){
				var url = vm.taskUrlPrefix + "/getTask";
				vm.currentFacilityHistoryItem = [];
				vm.getData(url,"GET",{
					taskId: id
				},function(data){
					if(data){
						vm.currentFacilityHistoryItem = data;
					}else{
						vm.currentFacilityHistoryItem = [];
					}
				},function(err){
					console.log(err);
				})
				vm.goToContent("facilityHistoryItemDetail","历史记录")
			}else{
				vm.currentFacilityHistoryItem = facilityHistoryItem;
				vm.goToContent("watchHistoryItemDetail","值守记录")
			}
		},
		goToFacility: function(){
			vm.goToContent('facility','设备列表',function(){
				vm.goBackIcon=false;
				vm.lastGoBackIcon=[];
				vm.lastContent=[];
				vm.lastTitle=[];
			})
		},
		goToTask: function(){
			vm.goToContent('task','任务列表',function(){
				vm.goBackIcon=false;
				vm.lastGoBackIcon=[];
				vm.lastContent=[];
				vm.lastTitle=[];
				vm.isShowHasNewTask = false;

				if(vm.userInfo.roleId == 3){
					vm.taskFilter = "acceptable";// 普通用户，默认显示可接受的任务
					vm.taskList = vm.taskList_acceptable;
				}else{
					vm.taskFilter = "myRelease";// 管理员用户，默认显示自己发布的任务
					vm.taskList = vm.taskList_myRelease;

					vm.getUserInspectDeviceType();
				}
			})
		},
		goToAlarm: function(){
			vm.goToContent('alarm','告警列表',function(){
				vm.goBackIcon=false;
				vm.lastGoBackIcon=[];
				vm.lastContent=[];
				vm.lastTitle=[];
				vm.alarmFilter="serious";
				vm.alarmList=vm.alarmList_serious;
				vm.isShowHasNewAlarm = false;
			})
		},
		goToMy: function(){
			vm.goToContent('myself','我的',function(){
				vm.goBackIcon=false;
				vm.lastGoBackIcon=[];
				vm.lastContent=[];
				vm.lastTitle=[];
			})
		},
		goBack: function(){
			if(vm.lastContent.length){
				vm.title = vm.lastTitle.pop();
				vm.content = vm.lastContent.pop();
				vm.goBackIcon = vm.lastGoBackIcon.pop();				
			}else{
				if(vm.exitAppTicker == 0){
                    vm.exitAppTicker++;
                    webToast("再点一次退出","middle",1000);
                    setTimeout(function(){
                            vm.exitAppTicker = 0;
                    },1000);
	            }else if(vm.exitAppTicker == 1){
	                navigator.app.exitApp(); //退出app
	            }
			}
		},
		goToFacilityAlarm: function(realName){
			var alarms = vm.alarmList_all_computed[realName];
			vm.currentFacilityAlarm = alarms;
			vm.goToContent('facilityAlarm',realName + " 告警");
		},
		initWebSocket: function(url,dataSendWhenOpened,messageHandler,closeHandler,errHandler){
			if(!url)return null;
			var socket = null;
			try {
                socket = new WebSocket(url);
            } catch (error) {
                console.log(error.message);
                return;
            }
            socket.onopen = function(event) {
                console.log("WebSocket is Open !");
                if(dataSendWhenOpened){
                	socket.send(dataSendWhenOpened);
                }        
            };
            socket.onclose = function(event) {
                console.log("WebSocket is Close !");
                if(closeHandler && typeof closeHandler === "function"){
                	closeHandler();
                }
            };
            socket.onerror = function(event) {
                console.error("WebSocket Error !");
                if(errHandler && typeof errHandler === "function"){
                	errHandler();
                }
            };
            socket.onmessage = function(event){
            	try{
					var data = event.data && JSON.parse(event.data) || [];
            	}catch(error){
            		console.log(error);
            		return;
            	}            	
				if(messageHandler && typeof messageHandler === "function"){
                	messageHandler(data);
                }
            }
            return socket;
		},
		extendFacilityWithAlarm: function(){
			vm.facilityList.forEach(function(facility){
				facility.alarms = [];
				vm.alarmList_all&& vm.alarmList_all.forEach(function(alarm){
					if(alarm.domainName && facility.domain === alarm.domainName){
						facility.alarms.push(alarm); 
					}				
				})
			})
			vm.facilityList.sort(function(a, b){
				return b.alarms.length - a.alarms.length;
			})
		},
		checkPreResult: function(item){
			if(!item.inspectResult.preResult){
				item.inspectResult.result = true;
			}
		},
		formatTime: function(time){
			// time必须是"2017-12-12 10:10:12"格式
			// 返回：相对今天的友好时间
			var a = time.split(" ");
			var nyr = a[0], sfm = a[1];
			var nyr_arr = nyr.split("-"), sfm_arr = sfm.split(":");
			var n = nyr_arr[0], y = nyr_arr[1], r = nyr_arr[2], s = sfm_arr[0], f = sfm_arr[1], m = sfm_arr[2];
			var now = new Date();
			var now_n = now.getFullYear(), now_y = now.getMonth()+1, now_r = now.getDate();
			if(n == now_n){
				if(y == now_y){
					if(now_r - r ==0){
						return "今天 " + s + "点" + f + "分";
					}else if(now_r - r ==1){
						return "昨天 " + s + "点" + f + "分";
			        }else{
						return "本月" + r + "日 " + s +"点" + f + "分"; 
					}
				}else{
					if(now_y - y ==1){
						return "上月" + r + "日 " + s +"点" + f + "分";
					}else{
						return y + "月" + r + "日 " + s +"点" + f + "分";
					}
				}					
			}else{
				if(now_n - n ==1){
					return "去年" + y + "月" + r + "日 " + s +"点" + f + "分";
				}else{
					return n + "年" + y + "月" + r + "日 " + s +"点" + f + "分";
				}				 
			}
		},
		stringifyTime: function(time){
			// time: 必须是标准Date时间
			// 返回：格式化时间字符串,如"2017-12-12 10:10:12"
			if(time instanceof Date){
				var now = new Date();
				var n = now.getFullYear(), 
					y = now.getMonth() + 1, 
					r = now.getDate(),
					s = now.getHours(),
					f = now.getMinutes(),
					m = now.getSeconds();
				return n + "-" +y +"-" + r + " "+s+":"+f+":"+m;
			}
			return "";
		},
		// 到站签到
		signBaseStation: function(position, success, fail){
			var currentLongitude = position.coords.longitude;
        	var currentLatitude = position.coords.latitude;
        	$.ajax({
        		url: vm.taskUrlPrefix + "/signBaseStation",
        		type: "GET",
        		data: {
        			facilityDomain: vm.currentFacility.domain,
        			longitude: currentLongitude,
        			latitude: currentLatitude
        		},
        		success: function(data){
        			if(data=="success"){
        				success();
        			}else{
        				webToast("签到失败","middle",1000);
        			}
        		},
        		fail: function(err){
        			console.log(err);
        			webToast("签到失败","middle",1000);
        		}
        	})
		},
		// 到站定位
		facilitySignin: function(){
			vm.getCurrentPosition(
				function(position){
		        	vm.isShowGettingCurrentPosition = false;
		        	vm.signBaseStation(position,
		        		function(){
		        			vm.isShowSelectInspectType_base = true;
	        				vm.isInspectWithGps = true;
		        			webToast("到站签到成功","middle",1000);
		        		},
		        		function(){

		        		}
		        	)
				},
				function(error){
					var label = "";
		        	switch(error.code){
		        		case error.TIMEOUT:
		        			label = "获取地理位置超时, \n是否先进行巡检，稍后再定位提交？";
		        			break;
		        		case error.PERMISSION_DENIED:
		        			label = "用户拒绝了位置服务, \n是否先进行巡检，稍后再定位提交？";
		        			break;
		        		case error.POSITION_UNAVAILABLE:
		        			label = "定位服务不可用, \n是否先进行巡检，稍后再定位提交？";
		        			break;
		        		default:
		        			label = "获取地理位置失败, \n是否先进行巡检，稍后再定位提交？";
		        	}
					vm.$confirm(label, "提示", {
		        		confirmButtonText: '确定',
	          			cancelButtonText: '取消',
	          			type: 'warning',
	          			center: true
		        	}).then(function(){
		        		vm.isShowSelectInspectType_base = true;
		        		vm.isInspectWithGps = false;
		        	}).catch(function(){

		        	})
				})
		},
		getCurrentPosition: function(success, fail){
			vm.isShowGettingCurrentPosition = true;
			//定位数据获取成功响应
	        function onSuccess(position) {
	        	if(success && typeof success == "function"){
	        		success(position);
	        	}
	        };

	        //定位数据获取失败响应
	        function onError(error) {
	        	vm.isShowGettingCurrentPosition = false;
	        	if(fail && typeof fail =="function"){
    				fail(error);
    			}	         	
	        }

	        //开始获取定位数据
	        navigator.geolocation.getCurrentPosition(onSuccess, onError, { 
	        	maximumAge: 3000, 
	        	timeout: 30000, 
	        	enableHighAccuracy: true 
	        });
		},
		getPhoto: function(onSuccess, onFail, options) {
		    //quality : 图像的质量，范围是[0,100]
		    navigator.camera.getPicture(
		        function(url){
		            onSuccess(url);
		        },
		        function(error){
		        	onFail(error);
		        },
		        options
		    );
		},
		addFaultPicture_local: function(faultItem){
			if(faultItem.photo.length>1){
				webToast("最多添加两张图片","middle",1000);
			}else{
				vm.getPhoto(
					function(picData){
						var data= "data:image/jpeg;base64," + picData;
						faultItem.photo.push(data);
					},
					function(error){
						webToast("未获得到照片","middle",1000);
					},
					{
						quality: 20,
						sourceType: Camera.PictureSourceType.PHOTOLIBRARY,
						destinationType: Camera.DestinationType.DATA_URL,
						targetHeight: 1334,
						targetWidth : 1334
					}
				);				
			}
		},
		addFaultPicture_camera: function(faultItem){
			if(faultItem.photo.length>1){
				webToast("最多添加两张图片","middle",1000);
			}else{
				vm.getPhoto(
					function(picData){
						var data = "data:image/jpeg;base64," + picData;
						faultItem.photo.push(data);
					},
					function(error){
						webToast("未获得到照片","middle",1000);
					},
					{
						quality: 20,
						sourceType: Camera.PictureSourceType.CAMERA,
						destinationType: Camera.DestinationType.DATA_URL,
						targetHeight: 1334,
						targetWidth : 1334
					}
				);
			}
			
		},
		showBigPicture: function(faultItem, picIndex, flag){
			vm.goToContent("showBigPicture","预览图片",function(){
				vm.currentFaultItem = faultItem;
				vm.currentFaultPic = picIndex;
				vm.bigPicture = faultItem.photo[picIndex];
				if(flag){
					vm.isShowDelPicBtn = false;
				}else{
					vm.isShowDelPicBtn = true;
				}
			})
		},
		removePicture: function(){
			vm.currentFaultItem.photo.splice(vm.currentFaultPic,1);
			vm.goBack();
		},
		historyItemInspectType: function(facilityHistoryItem){
			var type = "";
			switch(facilityHistoryItem.inspectType){
				case 0:
					type = "[自主]";
					break;
				case 1:
					type = "[巡检]";
					break;
				case 2:
					type = "[抢修]";
					break;
				case 10:
					type = "[值守]";
					break;
			}
			return type;
		},
		// 清空任务列表
		clearTaskList: function(){
			vm.taskList = null;
			vm.taskList_acceptable = null;
			vm.taskList_my = null;
			vm.taskList_myRelease = null;
		},
		// 清空告警列表
		clearAlarmList: function(){
			vm.alarmList = null;
			vm.alarmList_all = null;
			vm.alarmList_serious = null;
			vm.alarmList_normal = null;
			vm.alarmList_slight = null;
		},
		// 初始化任务websokcet
		initTaskWebSocket: function(){
			var url = window.location.host + window.location.pathname;
			var wsUrl = "ws://"+ vm.IPstorage +":8168/";
			var user = vm.user;
			var user_str = JSON.stringify(user);
			if(vm.taskSocket&& vm.taskSocket.close){
				vm.taskSocket.close();
			}
			var socket = vm.initWebSocket(wsUrl + "websocket/TaskHandler",user_str,vm.onTaskMessage,
				vm.clearTaskList,vm.clearTaskList
			);
			vm.taskSocket = socket;
		},
		// 初始化告警websokcet
		initAlarmWebSocket: function(){
			var url = window.location.host + window.location.pathname;
			var wsUrl = "ws://"+ vm.IPstorage +":8168/";
			var user = vm.user;
			var user_str = JSON.stringify(user);
			if(vm.alarmSocket&& vm.alarmSocket.close){
				vm.alarmSocket.close();
			}
			var socket = vm.initWebSocket(wsUrl + "websocket/AlarmHandler",user_str,vm.onAlarmMessage,
				vm.clearAlarmList,vm.clearAlarmList
			);
			vm.alarmSocket = socket;
		},
		// 任务websocket获取到消息的处理函数
		onTaskMessage: function(data){
			if(data=="failure") return;
			// 任务排序
			data.sort(function(a, b){
				return b.id - a.id;
			})
			// 判断新任务
			var hasNewTask = false;
			data.forEach(function(task){
				task.isOld = true;
				if(vm.checkedTask.indexOf(task.id)==-1){
					task.isOld = false;
					hasNewTask = true;
				}					
			})

			// 任务分类
			var acceptableTask = data.filter(function(task){
				return task.state == 0;
			})
			vm.taskList_acceptable = acceptableTask;

			var taskList_my = data.filter(function(task){
				return task.inspectBy == vm.user.userId && task.state != 0;
			})
			vm.taskList_my = taskList_my;

			var taskList_myRelease = data.filter(function(task){
				return task.releaseBy == vm.user.userId;
			})
			vm.taskList_myRelease = taskList_myRelease;

			if(vm.taskFilter == "acceptable") vm.taskList = vm.taskList_acceptable;
			if(vm.taskFilter == "my") vm.taskList = vm.taskList_my;
			if(vm.taskFilter == "myRelease") vm.taskList = vm.taskList_myRelease;

			if(hasNewTask) vm.isShowHasNewTask = true;

		},
		// 告警websocket获取到消息的处理函数
		onAlarmMessage: function(data){
			if(data=="failure") return;
			var serious = [],normal = [],slight = [];
			vm.alarmList_all = data;

			vm.extendFacilityWithAlarm();
			
			data.forEach(function(alarm){
				if(alarm.level == 0){
					serious.push(alarm);
				}
				if(alarm.level == 1){
					normal.push(alarm);
				}
				if(alarm.level == 2){
					slight.push(alarm);
				}
			})
			vm.alarmList_serious = serious;
			vm.alarmList_normal = normal;
			vm.alarmList_slight = slight;

			if(vm.alarmFilter=="all") vm.alarmList = vm.alarmList_all;
			if(vm.alarmFilter=="serious") vm.alarmList = vm.alarmList_serious;
			if(vm.alarmFilter=="normal") vm.alarmList = vm.alarmList_normal;
			if(vm.alarmFilter=="slight") vm.alarmList = vm.alarmList_slight;

			vm.isShowHasNewAlarm = true;
		}
	},
	computed:{
		facilityList_computed: function(){
			var vm = this;
			var condition = vm.facilitySearchCondition.toUpperCase(); 
			if(condition == "") return vm.facilityList;
			if(vm.facilityList.length == 0) return [];
			var facilityList = vm.facilityList;
			var list = facilityList.filter(function(facility){
				var name = facility.name;
				var firstPY = facility.firstPY;
				if(name.indexOf(condition)!=-1){
					return true;
				}
				for(var i=0,len=firstPY.length;i<len;i++){
					if(firstPY[i].toUpperCase().indexOf(condition)!=-1){
						return true;
					}
				}
				return false;
			})
			return list;
		},
		isSubmitInspectBtnUsable: function(){
			var isProcessingAfterCommitInspect = this.isProcessingAfterCommitInspect;
			var vm = this;
			if(vm.currentInspectType){
				var isEnabledFacilityInspect = vm.isEnabledFacilityInspect;
				var isEnabledFaultInspect = (function(){
					var bool = false;
					vm.faults.forEach(function(fault){
						if(fault.name && fault.name.name){
							bool = true;
						}
					})
					return bool;
				})();
				var isEnabledSelectInspectTask = (function(){
					if(vm.currentInspectBindTasks.length){
						return vm.currentSelectedInspectBindTasks.length;
					}else{
						return true;
					}
				})();
				return (!isProcessingAfterCommitInspect)&&isEnabledSelectInspectTask && (isEnabledFacilityInspect || isEnabledFaultInspect);
			}else{
				var duty = vm.dutys[0];
				var isEnabledContent = duty.content.length;
				var isEnabledPlace = duty.place.length;
				var isEnabledRecords = (function(){					
					var bool = false;
					duty.records.forEach(function(record){
						if(record.content.length>0){
							bool = true;
						}
					})
					return bool;
				})();
				return (!isProcessingAfterCommitInspect)&&isEnabledContent&& isEnabledPlace&& isEnabledRecords;
			}
		},
		isPublishTaskBtnUsable: function(){
			var taskPublish = this.taskPublish,b;
			if(taskPublish.type != 2){
				b = taskPublish.taskName &&taskPublish.taskDescription &&taskPublish.selectedFacilityIndex.length &&taskPublish.inspectDeviceTypeId.name;
			}else if(taskPublish.type == 2){
				b = taskPublish.taskName &&taskPublish.taskDescription &&taskPublish.inspectTime
			}
			
			return b;
		},
		selectedFacilityThumb: function(){
			var selectedFacilityThumb = this.taskPublish.selectedFacilityIndex;
			var len = selectedFacilityThumb.length;
			if(len >0){
				var firstSelectFacility = selectedFacilityThumb[0];
				var firstSelectFacilityName = "";
				this.facilityList.forEach(function(facility){
					if(facility.domain == firstSelectFacility){
						firstSelectFacilityName = facility.name;
					}
				})
				return firstSelectFacilityName + "(共"+len+"个)";
			}else{
				return "点击选择";
			}
		},
		selectedPerformerThumb: function(){
			var selectedPerformerThumb = this.taskPublish.selectedPerformerIndex;
			var len = selectedPerformerThumb.length;
			if(len >0){
				var firstSelectPerformer = selectedPerformerThumb[0];
				var firstSelectPerformerName = "";
				this.UserList.forEach(function(performer){
					if(performer.id == firstSelectPerformer){
						firstSelectPerformerName = performer.name;
					}
				})
				return firstSelectPerformerName + "(共"+len+"个)";
			}else{
				return "点击选择";
			}
		},
		currentFacility_computed: function(){
			var cf = this.currentFacility;
			var temp ={
				"域名": cf.domain,
				"ip地址": cf.ip,
				"系统号": cf.sysNumber,
				"控制信道频率号": cf.cchFreq,
				"逻辑信道数": cf.tchCount,
				"经度": cf.longitude,
				"纬度": cf.latitude,
				"厂家": cf.manufacturer,
				"地址": cf.address,
				"巡检状态": cf.status,
			}
			return temp;
		},
		currentFacilityHistoryItem_computed: function(){
			var i = this.currentFacilityHistoryItem;
			if(i.inspectType == 10 || i.type == 2){
				if(i.inspectUser!==undefined){
					var Records = JSON.parse(i.inspectRecords);
					var innerRecords = Records[0].records;

					var temp = {
						"值守人":i.inspectUser.name,
						"开始时间":i.inspectTime && vm.formatTime(i.inspectTime),
						"完成时间":i.endTime && vm.formatTime(i.endTime),
						"内容":Records[0].content,
						"地点":Records[0].place,
					};
					$.each(innerRecords,function(k){
						temp["记录"+(k+1)] = innerRecords[k].content
					})
					return temp;
				}
			}else{
				if(i.inspectUser!==undefined){
					var temp = {
						// "任务名称":i.taskName,
						// "描述":i.description,
						// "发布者":i.releaseUser.name,
						// "发布时间":i.releaseTime,
						"巡检人":i.inspectUser.name,
						"开始时间":i.inspectTime && vm.formatTime(i.inspectTime),
						"完成时间":i.endTime && vm.formatTime(i.endTime),
						// "记录":i.inspectRecords,
					}
					return temp;
				}
			}
		},
		currentFacilityHistoryItemRemarks_computed: function(){
			var i = this.currentFacilityHistoryItem;
			if(i.inspectRecords){
				var remarks = JSON.parse(i.inspectRecords);
				return remarks;
			}
			return [];
		},
		currentFacilityHistoryItemFaults_computed: function(){
			var i = this.currentFacilityHistoryItem;
			if(i.troubleShootInfo){
				var faults = JSON.parse(i.troubleShootInfo);
				return faults;
			}
			return [];
		},
		alarmList_computed: function(){
			var al = this.alarmList || [];
			var container = {};
			al.forEach(function(item){
				var realName = item.realName;
				if(!container[realName]){
					container[realName] = [];
					container[realName].push(item);
				}else{
					container[realName].push(item);
				}
			})
			return container;
		},
		alarmList_all_computed: function(){
			var al = this.alarmList_all || [];
			var container = {};
			al.forEach(function(item){
				var realName = item.realName;
				if(!container[realName]){
					container[realName] = [];
					container[realName].push(item);
				}else{
					container[realName].push(item);
				}
			})
			return container;
		},
		userInfo:function(){
			return this.user;
		},
		userList_filterByInspectDeviceType: function(){
			var userList = this.UserList;
			if(this.taskPublish.type == 1){
				var inspectType = this.taskPublish.inspectDeviceTypeId.name;
				userList = this.UserList.filter(function(user){
					var userInspectType;
					if(user.inspectDeviceTypeName){
						userInspectType = user.inspectDeviceTypeName.split(",");
					}else {
						userInspectType = [];
					}
					return userInspectType.indexOf(inspectType) != -1;
				})
			}
			
			return userList;
		},
	},
	mounted: function() {
		Vue.nextTick(function () {
			//绑定时间选择弹出层监听事件
			var myDate = new Date();
			var selectYear = myDate.getFullYear();
			var selectMonth = myDate.getMonth();
			var yearArr = [];
			for(var i=0;i<10;i++){				//绑定从现在起过去10年
				yearArr.push(selectYear-i);
			}
			console.log(yearArr.reverse());
			$.scrEvent2({
				data: yearArr.reverse(),
				data2: [01,02,03,04,05,06,07,08,09,10,11,12],
				evEle: '.timeStart',
				title: '开始时间',
				defValue: (yearArr.reverse())[9],
				defValue2: selectMonth+1,
				afterAction: function (data1, data2) {
					$('.timeStart,.timeEnd').text((data1 + '-') + ((data2<10) ? ('0'+data2) : data2));
				}
			})
			$.scrEvent2({
				data: yearArr.reverse(),
				data2: [01,02,03,04,05,06,07,08,09,10,11,12],
				evEle: '.timeEnd',
				title: '结束时间',
				defValue: (yearArr.reverse())[9],
				defValue2: selectMonth+1,
				afterAction: function (data1, data2) {
					$('.timeEnd').text((data1 + '-') + ((data2<10) ? ('0'+data2) : data2));
				}
			})

			$.scrEvent2({
				data: yearArr.reverse(),
				data2: [01,02,03,04,05,06,07,08,09,10,11,12],
				evEle: '.timeStart2',
				title: '开始时间',
				defValue: (yearArr.reverse())[9],
				defValue2: selectMonth+1,
				afterAction: function (data1, data2) {
					$('.timeStart2,.timeEnd2').text((data1 + '-') + ((data2<10) ? ('0'+data2) : data2));
				}
			})
			$.scrEvent2({
				data: yearArr.reverse(),
				data2: [01,02,03,04,05,06,07,08,09,10,11,12],
				evEle: '.timeEnd2',
				title: '结束时间',
				defValue: (yearArr.reverse())[9],
				defValue2: selectMonth+1,
				afterAction: function (data1, data2) {
					$('.timeEnd2').text((data1 + '-') + ((data2<10) ? ('0'+data2) : data2));
				}
			})

			$.scrEvent2({
				data: yearArr.reverse(),
				data2: [01,02,03,04,05,06,07,08,09,10,11,12],
				evEle: '.timeStart3',
				title: '开始时间',
				defValue: (yearArr.reverse())[9],
				defValue2: selectMonth+1,
				afterAction: function (data1, data2) {
					$('.timeStart3,.timeEnd3').text((data1 + '-') + ((data2<10) ? ('0'+data2) : data2));
				}
			})
			$.scrEvent2({
				data: yearArr.reverse(),
				data2: [01,02,03,04,05,06,07,08,09,10,11,12],
				evEle: '.timeEnd3',
				title: '结束时间',
				defValue: (yearArr.reverse())[9],
				defValue2: selectMonth+1,
				afterAction: function (data1, data2) {
					$('.timeEnd3').text((data1 + '-') + ((data2<10) ? ('0'+data2) : data2));
				}
			})

			document.getElementById("index").style.display="block";
			document.getElementById("mask").style.display="none";
			//实例创建后获取本地服务器IP
			vm.IPstorage = localStorage.getItem("ServerIp");
			if(vm.IPstorage == '' || vm.IPstorage == null){
				console.log("No IPstorage",vm.IPstorage);
				webToast('无法获取服务器IP,请重新登陆','middle',2000);
				setTimeout(function(){
					vm.UserExit();
				},1500)
			}else{
				//console.log("IPstorage",vm.IPstorage);
				vm.taskUrlPrefix= "http://" + vm.IPstorage + ":8168/rs/task";
				vm.logHttp= "http://" + vm.IPstorage + ":8168/rs/task/getUserTaskHistory";
				vm.userHttp= "http://" + vm.IPstorage + ":8168/rs/user/findUserList";
				vm.roleHttp= "http://" + vm.IPstorage + ":8168/rs/user/setUserRoles";
				vm.deviceGroupHttp="http://" + vm.IPstorage + ":8168/rs/facilityGroup/getFacilityGroup";
				vm.deviceHttp="http://" + vm.IPstorage + ":8168/rs/facilityGroup/getAllFacility";
				vm.setDeviceHttp="http://" + vm.IPstorage + ":8168/rs/facilityGroup/setFacilityGroup";
				vm.setDeviceGroupHttp="http://" + vm.IPstorage + ":8168/rs/user/setUserFacility";
				vm.delDeviceGroupHttp="http://" + vm.IPstorage + ":8168/rs/facilityGroup/delFacilityGroup";
				vm.modifyPsdHttp="http://" + vm.IPstorage + ":8168/rs/user/modifyPassword";
				vm.deviceTypeHttp="http://" + vm.IPstorage + ":8168/rs/inspectDeviceType/getInspectDeviceTypeList";
				vm.setDeviceTypeHttp="http://" + vm.IPstorage + ":8168/rs/user/setUserInspectDeviceType";
				vm.getDeviceTypeListHttp="http://" + vm.IPstorage + ":8168/rs/inspectDeviceType/getInspectDeviceTypeList";
				vm.addFacilityTypeHttp="http://" + vm.IPstorage + ":8168/rs/inspectDeviceType/setInspectDeviceType";
				vm.delFacilityTypeHttp="http://" + vm.IPstorage + ":8168/rs/inspectDeviceType/deleteInspectDeviceType";
				vm.getTypeMenuListHttp="http://" + vm.IPstorage + ":8168/rs/menu/getMenuList";
				vm.getTypeModuleListHttp="http://" + vm.IPstorage + ":8168/rs/module/getModuleList";
				vm.addFacilityTypeMenuHttp="http://" + vm.IPstorage + ":8168/rs/menu/setMenu";
				vm.delFacilityTypeMenuHttp="http://" + vm.IPstorage + ":8168/rs/menu/deleteMenu";
				vm.addFacilityTypeModuleHttp="http://" + vm.IPstorage + ":8168/rs/module/setModule";
				vm.delFacilityTypeModuleHttp="http://" + vm.IPstorage + ":8168/rs/module/deleteModule";
				vm.watchHttp="http://" + vm.IPstorage + ":8168/rs/task/getUserWatchHistory";
				vm.exportByUserHttp="http://" + vm.IPstorage + ":8168/rs/task/exportInspectExcelByUser";
				vm.getUserChartsHttp="http://" + vm.IPstorage + ":8168/rs/statistics/statisticsByUser";
				vm.getDeviceChartsHttp="http://" + vm.IPstorage + ":8168/rs/statistics/statisticsByDevice";
				vm.getHistoryDeviceList="http://" + vm.IPstorage + ":8168/rs/statistics/getFacilityMap";
				vm.getMyChartsHttp="http://" + vm.IPstorage + ":8168/rs/statistics/statisticsByUserAndFacility";
				vm.resetUserPsdHttp="http://" + vm.IPstorage + ":8168/rs/user/resetPassword";
				vm.logoutHttp="http://" + vm.IPstorage + ":8168/rs/user/logout";
			}
			//取得存于本地的用户登录信息
			vm.UserStorage = JSON.parse(localStorage.getItem("usermassage"));
			if(vm.UserStorage == {} || vm.UserStorage == null){
				console.log('no UserStorage',vm.UserStorage);
			}else{
				console.log('UserStorage',vm.UserStorage);
				vm.user.userId = vm.UserStorage.id;
				vm.user.userName = vm.UserStorage.name;
				vm.user.roleId = vm.UserStorage.roleId;
				vm.user.roleName = vm.UserStorage.role.name;
				vm.user.facilityGroupId = vm.UserStorage.facilityGroupId;
				vm.user.loginName = vm.UserStorage.loginName;
				vm.user.loginDate = vm.UserStorage.loginDate;
				vm.user.phone = vm.UserStorage.phone;
			}

			// 初始化websocket
			vm.initTaskWebSocket();
			vm.initAlarmWebSocket();
			// 初始化下拉刷新
			vm.facilityListRefresh = new MiniRefresh({
				container: "#facilityList",
				down:{
					isAuto: true,
					successAnim: {
						isEnable: true
					},
					isAllowAutoLoading: false,
					contentdown: "下拉刷新设备列表",
					callback: function(){
						vm.getFacilityList();
					}
				},
				up:{
					isLock: true,
					isAuto: false,
					callback: function(){
						vm.facilityListRefresh.endUpLoading(true)
					}
				}
			});
			vm.taskListRefresh = new MiniRefresh({
				container: "#taskList",
				down:{
					isAuto: false,
					successAnim: {
						isEnable: true
					},
					isAllowAutoLoading: false,
					contentdown: "下拉重新建立连接",
					callback: function(){
						vm.initTaskWebSocket();
						vm.taskListRefresh.endDownLoading(true);
					}
				},
				up:{
					isLock: true,
					isAuto: false,
					callback: function(){
						vm.taskListRefresh.endUpLoading(true)
					}
				}
			});
			vm.alarmListRefresh = new MiniRefresh({
				container: "#alarmList",
				down:{
					isAuto: false,
					successAnim: {
						isEnable: true
					},
					isAllowAutoLoading: false,
					contentdown: "下拉重新建立连接",
					callback: function(){
						vm.initAlarmWebSocket();
						vm.alarmListRefresh.endDownLoading(true);
					}
				},
				up:{
					isLock: true,
					isAuto: false,
					callback: function(){
						vm.alarmListRefresh.endUpLoading(true)
					}
				}
			});

			
        })
	},
	directives: {
		// focus: {
		// 	update: function (el,value) {
		// 		if(value){						
		// 			el.focus();
		// 			console.log(value);
		// 		}
		// 	}
		// },
		slide: {
			inserted: function(el, binding){
				var startX = 0, startY = 0, moveX = 0, disX = 0, disY = 0,
					maxSilde = binding.value || 100, // 滑动距离，默认100
					orientation = binding.arg || "left", // 滑动方向，默认左划，可取值：left right up down 
					flag = 0; // 滑动定点位置标示，0：原位置，1：已左划, 2: 已右划，3：已上划，4：已下滑
				console.log(orientation)
				el.addEventListener("touchstart",function(e){
					if(e.touches.length == 1){						
						this.style.transition = "none";
						this.style.webkitTransition = "none";
						startX = e.touches[0].pageX;
						startY = e.touches[0].pageY;
					}
				});
				el.addEventListener("touchmove",function(e){
					if(e.touches.length == 1){
						moveX = e.touches[0].pageX;
						moveY = e.touches[0].pageY;
						disX = startX - moveX;
						disY = startY - moveY;
						// console.log(disX, disY);
						switch(orientation){
							case "left":
								if(disX > maxSilde){
									disX = maxSilde;
								}else if(disX >= 0 && disX <= maxSilde && flag == 0){// 往左划
									el.style.transform = "translateX(-" + disX + "px)";
									el.style.webkitTransform = "translateX(-" + disX + "px)";
								}else if(disX < 0 && disX > (-maxSilde) && flag == 1){// 往右划
									el.style.transform = "translateX(" + (-disX-maxSilde) + "px)";
									el.style.webkitTransform = "translateX(" + (-disX-maxSilde) + "px)";
								}else if(disX <= (-maxSilde)){
									disX = 0;
								}
								break;
							case "right":
								if(disX > maxSilde){
									disX = maxSilde;
								}else if(disX >= 0 && disX <= maxSilde && flag == 2){// 往左划
									el.style.transform = "translateX(" + (maxSilde-disX) + "px)";
									el.style.webkitTransform = "translateX(" + (maxSilde-disX) + "px)";
								}else if(disX < 0 && disX > (-maxSilde) && flag == 0){// 往右划
									el.style.transform = "translateX(" + (-disX) + "px)";
									el.style.webkitTransform = "translateX(" + (-disX) + "px)";
								}else if(disX <= (-maxSilde)){
									disX = -maxSilde;
								}
								break;
							case "up":
								if(disY > maxSilde){
									disY = maxSilde;
								}else if(disY >=0 && disY <= maxSilde && flag == 0){// 往上划
									el.style.transform = "translateY(-" + disY +"px)";
									el.style.webkitTransform = "translateY(-" + disY +"px)";
								}else if(disY < 0 && disY > (-maxSilde) && flag == 3){// 往下滑
									el.style.transform = "translateY(" + (-maxSilde-disY) +"px)";
									el.style.webkitTransform = "translateY(" + (-maxSilde-disY) +"px)";
								}else if(disY <= (-maxSilde)){
									disY = 0;
								}
								break;
							case "down":
								if(disY > maxSilde){
									disY = maxSilde;
								}else if(disY >= 0 && disY <= maxSilde && flag == 4){// 往上划
									el.style.transform = "translateY(" + (maxSilde-disY) + "px)";
									el.style.webkitTransform = "translateY(" + (maxSilde-disY) + "px)";
								}else if(disY < 0 && disY > (-maxSilde) && flag == 0){// 往下划
									el.style.transform = "translateY(" + (-disY) + "px)";
									el.style.webkitTransform = "translateY(" + (-disY) + "px)";
								}else if(disY <= (-maxSilde)){
									disY = -maxSilde;
								}
								break;
						}
						
					}
				});
				el.addEventListener("touchend",function(e){
					if(e.changedTouches.length == 1){
						this.style.transition = "transform .3s";
						switch(orientation){
							case "left":
								if(disX >= maxSilde/2 && disX <= maxSilde && flag == 0){
									el.style.transform = "translateX(-"+ maxSilde +"px)";
									el.style.webkitTransform = "translateX(-"+ maxSilde +"px)";
									flag = 1;
								}else if(disX >= 0 && disX < maxSilde/2 && flag == 0){
									el.style.transform = "translateX(0px)";
									el.style.webkitTransform = "translateX(0px)";
									flag = 0;
								}else if(disX < 0 && disX > -maxSilde/2 && flag == 1){
									el.style.transform = "translateX(-"+ maxSilde +"px)";
									el.style.webkitTransform = "translateX(-"+ maxSilde +"px)";
									flag = 1;
								}else if(disX <= -maxSilde/2 && flag == 1){
									el.style.transform = "translateX(0px)";
									el.style.webkitTransform = "translateX(0px)";
									flag = 0;
								}
								break;
							case "right":
								if(disX >= maxSilde/2 && disX <= maxSilde && flag == 2){
									el.style.transform = "translateX(0px)";
									el.style.webkitTransform = "translateX(0px)";
									flag = 0;
								}else if(disX >= 0 && disX < maxSilde/2 && flag == 2){
									el.style.transform = "translateX("+ maxSilde +"px)";
									el.style.webkitTransform = "translateX("+ maxSilde +"px)";
									flag = 2;
								}else if(disX < 0 && disX > -maxSilde/2 && flag == 0){
									el.style.transform = "translateX(0px)";
									el.style.webkitTransform = "translateX(0px)";
									flag = 0;
								}else if(disX <= -maxSilde/2 && flag == 0){
									el.style.transform = "translateX("+ maxSilde +"px)";
									el.style.webkitTransform = "translateX("+ maxSilde +"px)";
									flag = 2;
								}
								break;
							case "up":
								if(disY >= maxSilde/2 && disY <= maxSilde && flag == 0){
									el.style.transform = "translateY(-"+ maxSilde +"px)";
									el.style.webkitTransform = "translateY(-"+ maxSilde +"px)";
									flag = 3;
								}else if(disY >= 0 && disY < maxSilde/2 && flag == 0){
									el.style.transform = "translateY(0px)";
									el.style.webkitTransform = "translateY(0px)";
									flag = 0;
								}else if(disY < 0 && disY > -maxSilde/2 && flag == 3){
									el.style.transform = "translateY(-"+ maxSilde +"px)";
									el.style.webkitTransform = "translateY(-"+ maxSilde +"px)";
									flag = 3;
								}else if(disY <= -maxSilde/2 && flag == 3){
									el.style.transform = "translateY(0px)";
									el.style.webkitTransform = "translateY(0px)";
									flag = 0;
								}
								break;
							case "down":
								if(disY >= maxSilde/2 && disY <= maxSilde && flag == 4){
									el.style.transform = "translateY(0px)";
									el.style.webkitTransform = "translateY(0px)";
									flag = 0;
								}else if(disY >= 0 && disY < maxSilde/2 && flag == 4){
									el.style.transform = "translateY("+ maxSilde +"px)";
									el.style.webkitTransform = "translateY("+ maxSilde +"px)";
									flag = 4;
								}else if(disY < 0 && disY > -maxSilde/2 && flag == 0){
									el.style.transform = "translateY(0px)";
									el.style.webkitTransform = "translateY(0px)";
									flag = 0;
								}else if(disY <= -maxSilde/2 && flag == 0){
									el.style.transform = "translateY("+ maxSilde +"px)";
									el.style.webkitTransform = "translateY("+ maxSilde +"px)";
									flag = 4;
								}
								break;
						}
						
					}
				});

			}
		}
	}
});

