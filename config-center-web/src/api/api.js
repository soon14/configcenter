import axios from "../common/axios";

//写绝对地址
let base = '/center';

//登录
export const requestLogin = params => { return axios.post(`${base}/oa/login`, params) };
//登出
export const requestLogout = params => { return axios.post(`${base}/oa/logout`, params) };
//菜单
export const getMenuList = params => { return axios.post(`${base}/oa/menu-tree`, params) };

//获取下拉框的数据
export const getSelectData = (params) => { return axios.get(`${base}/data/getSelectData?key=${params}`) };

//用户管理
//获得所有的用户
export const getUserListPage = params => { return axios.post(`${base}/user/getUserInfoAndRole`, params) };
//注册
export const postRegister = params => { return axios.post(`${base}/user/register`, params) };
//注销账户
export const closeAccount = params => { return axios.post(`${base}/user/closeAccount`, params) };
//审核用户
export const auditAccount = params => { return axios.post(`${base}/user/auditAccount`, params) };
//获取验证码
export const getAuthCode = params => { return axios.get(`${base}/user/getAuthCode` + params) };
//清理缓存
export const cleanCache = params => { return axios.get(`${base}/data/cleanCache`, params) };

//获取所有用户列表
export const getUserList = params => { return axios.post(`${base}/user/getUserList`, params) };

//更新基本信息
export const updateUserInfo = params => { return axios.post(`${base}/user/updateUserInfo`, params) };
//更新密码
export const updatePassword = params => { return axios.post(`${base}/user/updatePassword`, params) };
//重置密码
export const resetPassword = params => { return axios.post(`${base}/user/resetPassword`, params) };

//获取用户操作日志
export const getOperateLog = params => { return axios.post(`${base}/log/getOperateLog`, params) };

//应用管理
//应用的新增
export const createApp = params => { return axios.post(`${base}/app/createApp`, params) };
//获取应用
export const getMyApps = params => { return axios.post(`${base}/app/getMyApps`, params) };
//删除应用POST /center/app/delApp
export const deleteApp = params => { return axios.post(`${base}/app/delApp`, params) };
//应用的修改POST /center/app/modApp
export const updateApp = params => {return axios.post(`${base}/app/modApp`, params)};
//应用添加用户/center/app/addUser
export const appAddUser = params => {return axios.post(`${base}/app/addUser`, params)};
//应用修改用户角色/center/app/modUser
export const appModUser = params => {return axios.post(`${base}/app/modUser`, params)};

//环境管理
//环境的新增POST /center/appEnv/createAppEnv
export const createAppEnv = params => {return axios.post(`${base}/appEnv/createAppEnv`, params)};
//获取环境POST /center/appEnv/getAppEnv
export const getAppEnvs = params => {return axios.post(`${base}/appEnv/getAppEnv`,params)};
//删除环境POST /center/appEnv/deleteAppEnv
export const deleteAppEnv = params => {return axios.post(`${base}/appEnv/deleteAppEnv`,params)};
//修改环境POST /center/appEnv/updateAppEnv
export const updateAppEnv = params => {return axios.post(`${base}/appEnv/updateAppEnv`, params)};

//消息处理
//消费消息
export const consumeNotification = params => { return axios.post(`${base}/notify/consumeNotification`, params) };
//获取消息
export const getNotificationInfo = params => { return axios.post(`${base}/notify/getNotificationInfo`, params) };

//应用人员的管理
//获取人员POST /center/app/getAppUsers
export const getAppUsers = params => {return axios.post(`${base}/app/getAppUsers`, params)};
//删除对应的人员POST /center/app/delUser
export const deleteAppUser = params => {return axios.post(`${base}/app/delUser`, params)};


//配置文件的管理
//配置文件列表的查询/center/configFile/getConfigFiles
export const getFileConfigs = params => {return axios.post(`${base}/configFile/getConfigFiles`,params)};
//单个配置文件的删除/center/configFile/delConfigFile
export const deleteConfigFile = params => {return axios.post(`${base}/configFile/delConfigFile`,params)};
//替换配置文件/center/configFile/upConfigFileAndUpdate
export const upConfigFileAndUpdate = params => {return axios.post(`${base}/configFile/upConfigFileAndUpdate`,params)};
//复制配置文件/center/configFile/copyConfigFile
export const copyConfigFile = params => {return axios.post(`${base}/configFile/copyConfigFile`,params)};
//批量配置文件的删除/center/configFile/
export const batchDeleteConfigFile = params => {return axios.post(`${base}/configFile/delConfigFile`,params)};
//单个文件上传/center/configFile/upConfigFileAndCreate
export const uploadConfigFile = params => {return axios.post(`${base}/configFile/upConfigFileAndCreate`,params)};
//压缩文件上传/center/configFile/upConfigFileZipAndCreate
export const uploadConfigFileYaSuo = params => {return axios.post(`${base}/configFile/upConfigFileZipAndCreate`,params)};
//获取配置文件的内容POST /center/configFile/getConfigFilesContent
export const getConfigFilesContent = params => { return axios.post(`${base}/configFile/getConfigFilesContent`, params)};
//在线编辑配置文件保存 POST /center/configFile/saveConfigFileContent
export const saveConfigFileContent = params => { return axios.post(`${base}/configFile/saveConfigFileContent`, params)};
//获取配置文件历史版本信息POST /center/configFile/getFileHis
export const getFileHistory = params => { return axios.post(`${base}/configFile/getFileHis`, params)};
//获取指定配置文件最新版本内容
export const getConfigFileLastVersion = params => { return axios.post(`${base}/configFile/getLastVersion`, params)};
//获取临时配置文件
export const getTempFileContent = params =>{ return axios.post(`${base}/configFile/getTempFileContent`, params) };
//获取压缩包配置文件center/configFile/getAllConfigFile
export const getAllConfigFile = params =>{ return axios.get(`${base}/configFile/getAllConfigFile?envId=`+ params) };
//修改配置文件策略/center/configFile/changeStrategy
export const changeStrategy = params =>{ return axios.post(`${base}/configFile/changeStrategy`, params) };


//实例的管理
//实例的查询POST /center/ins/getIns
export const getInstances = params => {return axios.post(`${base}/ins/getIns`, params)};
//查看实例的文件POST /center/ins/getInsConfigInfo
export const getInstancesConfigInfo = params => {return axios.post(`${base}/ins/getInsConfigInfo`,params)};
//移除断开的实例POST /center/ins/deleteIns
export const deleteInstance = params => {return axios.post(`${base}/ins/deleteIns`, params)};

//配置管理
//上传文件的接口
export const uploadFile = params => {return axios.post(`${base}/configFile/uploadConfigFile`,params)};
//配置项查询
export const getConfigItems = params => { return axios.post(`${base}/configItem/getConfigItems`, params) };
//配置项新增
export const addConfigItem = params =>{ return axios.post(`${base}/configItem/addConfigItem`, params)};
//配置项删除
export const delConfigItem = params =>{ return axios.post(`${base}/configItem/delConfigItem`, params)};
//配置项修改
export const modConfigItem = params =>{ return axios.post(`${base}/configItem/modConfigItem`, params)};
//配置项复制
export const copyConfigItem = params =>{ return axios.post(`${base}/configItem/copyConfigItem`, params)};
//获取历史配置项
export const getConfigItemHis = params =>{ return axios.post(`${base}/configItem/getConfigItemHis`, params) };
//获取指定版本配置项
export const getVersionContent = params =>{ return axios.post(`${base}/configItem/getVersionContent`, params) };
//根据配置查询实例POST /center/ins/getInsByConfig
export const getInsByConfig = params => {return axios.post(`${base}/ins/getInsByConfig`, params)};


//待提交任务
//获取用户当前环境下的待提交任务POST /center/task/getConfigTask
export const getConfigTaskDetail = params => {return axios.post(`${base}/task/getConfigTaskDetail`, params)};
export const getPushTaskDetail = params => {return axios.post(`${base}/task/getPushTaskDetail`, params)};
//获取下一审核节点所有可选审核人信息/center/task/getNextOperatorInfo
export const getNextOperatorInfo = params => {return axios.post(`${base}/task/getNextOperatorInfo`, params)};
//提交任务POST /center/task/submitTask
export const submitTask = params => {return axios.post(`${base}/task/submitTask`, params)};
//获取我提交的所有任务
export const getSubmitTask = params => {return axios.post(`${base}/task/getSubmitTask`, params)};
//获取等待审核的所有任务
export const getInReviewTask = params => {return axios.post(`${base}/task/getInReviewTask`, params)};
//获取所有已审核任务
export const getReviewedTask = params => {return axios.post(`${base}/task/getReviewedTask`, params)};
//审核任务
export const dealTask = params => {return axios.post(`${base}/task/dealTask`, params)};
//获取任务流程
export const getTaskFlow = params => {return axios.get(`${base}/task/getTaskFlow` + params)};
//撤销待提交任务 POST /center/task/rollbackTask
export const rollbackTask = params => {return axios.post(`${base}/task/rollbackTask`, params)};
//撤销修改文件方式POST /center/task/rollbackTaskDetail
export const rollbackTaskDetail = params => {return axios.post(`${base}/task/rollbackTaskDetail`, params)};
//获取配置策略变更任务详情
export const getStrategyChangeDetail = params => {return axios.get(`${base}/task/getStrategyChangeDetail` + params)};
//获取配置内容变更任务详情
export const getConfigChangeDetail = params => {return axios.get(`${base}/task/getConfigChangeDetail` + params)};
//getTaskTimeAndIns
export const getTaskTimeAndIns= params => {return axios.post(`${base}/task/getTaskTimeAndIns`, params)};

//刷新策略
//创建更新策略POST /center/strategy/createUpdateStrategy
export const createUpdateStrategy = params => {return axios.post(`${base}/strategy/createUpdateStrategy`, params)};
//创建更新策略(构造器) POST /center/strategy/createStrategyConstructor
export const createStrategyConstructor = params => {return axios.post(`${base}/strategy/createStrategyConstructor`, params)};
//创建更新策略(类变量)POST /center/strategy/createStrategyField
export const createStrategyField = params => {return axios.post(`${base}/strategy/createStrategyField`, params)};
//创建更新策略(方法)POST /center/strategy/createStrategyMethod
export const createStrategyMethod = params => {return axios.post(`${base}/strategy/createStrategyMethod`, params)};
//更新策略步骤（步数、参数值）POST /center/strategy/POST /center/strategy/updateStrategyStepNums
export const updateStrategyStepNums = params => {return axios.post(`${base}/strategy/updateStrategyStepNums`, params)};
//查询策略POST /center/strategy/queryStrategy
export const queryStrategy = params => {return axios.post(`${base}/strategy/queryStrategy`,params)};
//根据策略主键查询策略所有信息(包括步骤)POST /center/strategy/getUpdateStrategyAllInfo
export const getUpdateStrategyAllInfo = params => {return axios.post(`${base}/strategy/getUpdateStrategyAllInfo`,params)};
//删除策略POST /center/strategy/deleteConfigUpdateStrategy
export const deleteConfigUpdateStrategy = params => {return axios.post(`${base}/strategy/deleteConfigUpdateStrategy`,params)};
//删除策略步骤POST /center/strategy/deleteStrategyStep
export const deleteStrategyStep = params => {return axios.post(`${base}/strategy/deleteStrategyStep`,params)};
//center/strategy/getStrategyName
export const getStrategyName = params => {return axios.post(`${base}/strategy/getStrategyName`,params)};
//查询用户是否有新增更新策略的权限GET /center/strategy/checkUserPermission
export const checkUserPermission = params => {return axios.post(`${base}/strategy/checkUserPermission`,params)};

//推送配置 POST /center/push/pushConfig
export const pushConfig = params => {return axios.post(`${base}/push/pushConfig`,params)};
//获取所有一级组织数据GET /center/org/getLevelOneOrg
export const getAllLevelOneOrg = params => {return axios.post(`${base}/org/getLevelOneOrg`,params)};
//获取当前组织（无权限）
export const getCompanyList = params => { return axios.post(`${base}/org/getAllLevelOneOrg`, params) };
//增加组织POST /center/org/addOrg
export const addOrg = params => {return axios.post(`${base}/org/addOrg`,params)};
//修改组织POST /center/org/modOrg
export const modOrg = params => {return axios.post(`${base}/org/modOrg`,params)};
//删除组织POST /center/org/delOrg
export const delOrg = params => {return axios.post(`${base}/org/delOrg`,params)};
//post /center/org/queryUsers
export const queryUsers  = params => {return axios.post(`${base}/org/queryUsers`,params)};
//post /center/org/moveUser
export const moveUser   = params => {return axios.post(`${base}/org/moveUser`,params)};
//修改组织内用户角色 POST /center/org/modUserRole
export const modUserRole = params => { return axios.post(`${base}/org/modUserRole`, params) };
//获取组织的详细信息GET /center/org/getOrgInfo
export const getOrgInfo = params => {return axios.post(`${base}/org/getOrgInfo?orgId=${params}`)};
//根据组织ID获取组织内所有人员GET /center/org/getOrgUsers
export const getOrgUsers = params => {return axios.post(`${base}/org/getOrgUsers?orgId=${params}`)};
//获取所有组织角色 GET /center/org/getOrgRole
export const getOrgRole = params => { return axios.post(`${base}/org/getOrgRole`, params) };

//审核策略
//获取环境审核策略POST /center/audit/getAuditStrategy
export const getAuditStrategy = params => { return axios.post(`${base}/audit/getAuditStrategy`, params)};
//获取所有组织对应的组织树 POST /center/audit/getOrgTree
export const getOrgTree = params => {return axios.post(`${base}/audit/getOrgTree`, params)};
//增加审核策略步骤POST /center/audit/addAuditStrategyStep
export const addAuditStrategyStep = params => {return axios.post(`${base}/audit/addAuditStrategyStep`, params)};
//判断用户是否有修改权限POST /center/audit/checkPermission
export const checkPermission = params => {return axios.post(`${base}/audit/checkPermission`, params)};
//修改审核策略步骤POST /center/audit/modAuditStrategyStep
export const modAuditStrategyStep = params => {return axios.post(`${base}/audit/modAuditStrategyStep`, params)};
//删除审核策略步骤POST /center/audit/delAuditStrategyStep
export const delAuditStrategyStep = params => {return axios.post(`${base}/audit/delAuditStrategyStep`, params)};

//首页POST /center/home/getHomePageData
export const getHomePageData = params => {return axios.post(`${base}/home/getHomePageData`, params)};
//添加用户最近访问应用POST /center/home/addRecentApp
export const addRecentApp = params => {return axios.post(`${base}/home/addRecentApp`,params)};
