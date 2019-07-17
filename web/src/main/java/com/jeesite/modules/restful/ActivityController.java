package com.jeesite.modules.restful;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.biz.entity.BizActivity;
import com.jeesite.modules.biz.service.BizActionPlanService;
import com.jeesite.modules.biz.service.BizActivityService;
import com.jeesite.modules.biz.service.BizFixedPlanService;
import com.jeesite.modules.biz.service.BizPoliceService;
import com.jeesite.modules.biz.service.BizSolutionService;
import com.jeesite.modules.restful.dto.Result;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;

import io.swagger.annotations.ApiParam;

@RestController("ActivityController-v1")
@RequestMapping(value = "/api")
public class ActivityController {
	@Autowired
	private BizPoliceService bizPoliceService;
	@Autowired
	private BizActivityService bizActivityService;
	@Autowired
	private BizSolutionService bizSolutionService; 
	@Autowired
	private BizFixedPlanService bizFixedPlanService;
	@Autowired
	private BizActionPlanService bizActionPlanService; 
	
	@RequestMapping(value = {"/query"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> query(@ApiParam(value = "活动名称", required = false)@RequestParam(value = "activityName" , required = false)String activityName,
			@ApiParam(value = "活动日期(yyyy-MM-dd)", required = false)@RequestParam(value = "activityDate", required = false)String activityDate,
			@ApiParam(value = "条数限制", required = false)@RequestParam(value = "limit", required = false)String limit){
		Result r=new Result();
		String andsql="";
		if(StringUtils.isNotBlank(activityName)) {
			andsql="and p.activity_name like '%"+activityName+"%'";
		}
		if(StringUtils.isNotBlank(activityDate)) {
			andsql+="and p.activity_date = parsedatetime('"+activityDate+"','yyyy-MM-dd')";
		}
		String maxnum=" limit 1000";
		if(StringUtils.isNotBlank(limit)) {
			maxnum=" limit "+limit;
		}
		HashMap<String,Object> param=new HashMap<String,Object>();
		param.put("andsql",andsql);
		param.put("maxnum",maxnum);
		List<Map<String, Object>> list=bizActivityService.queryMap(param);
		List<DictData> serviceLevels=DictUtils.getDictList("sys_biz_service_level");
		List<DictData> securityModes=DictUtils.getDictList("sys_biz_security_mode");
		List<DictData> riskLevels=DictUtils.getDictList("sys_biz_risk_level");
		list.forEach(map->{
			if(map.containsKey("activityDate")) {
				Object date=map.get("activityDate");
				if(date!=null&&StringUtils.isNotBlank(date.toString())) {
					try {
						map.put("activityDate", DateUtils.formatDate((java.util.Date)date,"yyyy-MM-dd"));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			if(map.containsKey("serviceLevel")) {
				Object serviceLevel=map.get("serviceLevel");
				if(serviceLevel!=null&&StringUtils.isNotBlank(serviceLevel.toString())) {
					serviceLevels.forEach(dd->{
						if(dd.getDictValue().equals(serviceLevel)) {
							map.put("serviceLevelName",dd.getDictLabel());
						}
					});
				}
			}
			if(map.containsKey("securityMode")) {
				Object securityMode=map.get("securityMode");
				if(securityMode!=null&&StringUtils.isNotBlank(securityMode.toString())) {
					securityModes.forEach(dd->{
						if(dd.getDictValue().equals(securityMode)) {
							map.put("securityModeName",dd.getDictLabel());
						}
					});
				}
			}
			if(map.containsKey("riskLevel")) {
				Object riskLevel=map.get("riskLevel");
				if(riskLevel!=null&&StringUtils.isNotBlank(riskLevel.toString())) {
					riskLevels.forEach(dd->{
						if(dd.getDictValue().equals(riskLevel)) {
							map.put("riskLevelName",dd.getDictLabel());
						}
					});
				}
			}
		});
		r.setData(list);
		if(r.getData()==null) {
			r.setSuccess(false);
			r.setErrCode(Result.ERR_CODE);
			r.setMsg(Result.FAIL);
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	@RequestMapping(value = {"/activity"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getActivity(@ApiParam(value = "活动编号", required = true)@RequestParam(value = "activityCode")String activityCode) {
		Result r=new Result();
		try {
			HashMap<String,Object> param=new HashMap<String,Object>();
			param.put("andsql","and p.activity_code='"+activityCode+"'");
			List<Map<String, Object>> activityList=bizActivityService.queryMap(param);
			Map<String,Object> activity=activityList==null||activityList.size()==0?new HashMap<String,Object>():activityList.get(0);
			List<DictData> serviceLevels=DictUtils.getDictList("sys_biz_service_level");
			List<DictData> securityModes=DictUtils.getDictList("sys_biz_security_mode");
			List<DictData> riskLevels=DictUtils.getDictList("sys_biz_risk_level");
			if(activity.containsKey("activityDate")) {
				Object date=activity.get("activityDate");
				if(date!=null&&StringUtils.isNotBlank(date.toString())) {
					try {
						activity.put("activityDate", DateUtils.formatDate((java.util.Date)date,"yyyy-MM-dd"));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			if(activity.containsKey("serviceLevel")) {
				Object serviceLevel=activity.get("serviceLevel");
				if(serviceLevel!=null&&StringUtils.isNotBlank(serviceLevel.toString())) {
					serviceLevels.forEach(dd->{
						if(dd.getDictValue().equals(serviceLevel)) {
							activity.put("serviceLevelName",dd.getDictLabel());
						}
					});
				}
			}
			if(activity.containsKey("securityMode")) {
				Object securityMode=activity.get("securityMode");
				if(securityMode!=null&&StringUtils.isNotBlank(securityMode.toString())) {
					securityModes.forEach(dd->{
						if(dd.getDictValue().equals(securityMode)) {
							activity.put("securityModeName",dd.getDictLabel());
						}
					});
				}
			}
			if(activity.containsKey("riskLevel")) {
				Object riskLevel=activity.get("riskLevel");
				if(riskLevel!=null&&StringUtils.isNotBlank(riskLevel.toString())) {
					riskLevels.forEach(dd->{
						if(dd.getDictValue().equals(riskLevel)) {
							activity.put("riskLevelName",dd.getDictLabel());
						}
					});
				}
			}
			List<Map<String, Object>> solution=bizSolutionService.queryMap(new HashMap<String,Object>(){{put("andsql"," and p.activity='"+activityCode+"'");}});
			List<DictData> sides=DictUtils.getDictList("sys_biz_side_type");
			solution.forEach(map->{
				if(map.containsKey("persons")) {
					Object personCodes=map.get("persons");
					if(personCodes!=null&&StringUtils.isNotBlank(personCodes.toString())) {
						List<String> list =Arrays.asList(personCodes.toString().split(","));
						ArrayList<String> codes=new ArrayList<String>();
						list.forEach(code->{
							codes.add("'"+code+"'");
						});
						List<Map<String, Object>> br=bizPoliceService.queryMap(new HashMap<String,Object>(){{put("andsql","and p.police_code in("+String.join(",",  codes.toArray(new String[codes.size()]))+") ");}});
						codes.clear();
						br.forEach(m->{
					    	codes.add(String.valueOf(m.get("policeName")));
					    });
						map.put("policeNames", String.join(",",codes.toArray(new String[codes.size()])));
					}
				}
				Object side=map.get("side");
				if(side!=null&&StringUtils.isNotBlank(side.toString())) {
					sides.forEach(dd->{
						if(dd.getDictValue().equals(side)) {
							map.put("sideName",dd.getDictLabel());
						}
					});
				}
			});
			List<Map<String, Object>> fixedPlan=bizFixedPlanService.queryMap(new HashMap<String,Object>(){{put("andsql"," and p.activity='"+activityCode+"'");}});
			List<DictData> planTypes=DictUtils.getDictList("sys_biz_plan_type");
			fixedPlan.forEach(map->{
//				/ss/userfiles/fileupload/201905/1131225830389739520.jpg
				String fileUrl="";
				if(map.get("filePath")!=null&&map.get("fileId")!=null&&map.get("fileExtension")!=null) {
					fileUrl="/userfiles/fileupload/"+map.get("filePath")+""+map.get("fileId")+"."+map.get("fileExtension");
				}
				map.put("fileUrl", fileUrl);

				Object planType=map.get("planType");
				if(planType!=null&&StringUtils.isNotBlank(planType.toString())) {
					planTypes.forEach(dd->{
						if(dd.getDictValue().equals(planType)) {
							map.put("planTypeName",dd.getDictLabel());
						}
					});
				}
			});
			
			List<Map<String, Object>> actionPlan=bizActionPlanService.queryMap(new HashMap<String,Object>(){{put("andsql"," and p.activity='"+activityCode+"'");}});
			List<DictData> actionTypes=DictUtils.getDictList("sys_biz_action_type");
			actionPlan.forEach(map->{
				Object actionType=map.get("actionType");
				if(actionType!=null&&StringUtils.isNotBlank(actionType.toString())) {
					actionTypes.forEach(dd->{
						if(dd.getDictValue().equals(actionType)) {
							map.put("actionTypeName",dd.getDictLabel());
						}
					});
				}
			});
			List<DictData> dicts=DictUtils.getDictList("sys_biz_control_area");
			ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
			dicts.forEach(new Consumer<DictData>() {
				@Override
				public void accept(DictData t) {
					HashMap<String,Object> m=new HashMap<String,Object>();
					m.put("dictLabel", t.getDictLabel());
					m.put("dictValue", t.getDictValue());
					m.put("order", t.getTreeSort());
					m.put("description", t.getDescription());
					list.add(m);
				}
			});
			list.sort(new Comparator<HashMap<String,Object>>(){
					public int compare(HashMap<String,Object> arg0, HashMap<String,Object> arg1) {
							if(arg0.get("order")==null || arg0.get("order")==null) return 0;
							return Integer.valueOf(arg0.get("order").toString().trim()).compareTo(Integer.valueOf(arg1.get("order").toString().trim()));
					}
			});
			
			HashMap<String,Object> m=new HashMap<String,Object>();
			m.put("activity", activity);
			m.put("solution", solution);
			m.put("fixedPlan", fixedPlan);
			m.put("actionPlan", actionPlan);
			m.put("controlArea", list);
			r.setData(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(r.getData()==null) {
			r.setSuccess(false);
			r.setErrCode(Result.ERR_CODE);
			r.setMsg(Result.FAIL);
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
}
