<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>

<style type="text/css">

.drag {
  width: 100%;
  height: 300px;
  display: inline-block;
  border:1px;
  border-color: black;	
}

.dropCanvas {
  width: 49%;
  height: 100%;
  display: inline-block;
}

#boxB{
  margin-right: 0px;
  background:yellow;
}
#boxC{
  margin-right: 0px;
  background:green;
}
#boxD{
  margin-right: 0px;
  background:orange;
}
#big {
  width: 100%;
  height: 100%;
  background: white;
}

</style>

<script type="text/javascript">

//var index = 5;
var pos;

/*
var divPos = [];
$(".drag").each(function(i,e){
	console.log(e.id+"/"+$(e).position().left+"/"+$(e).position().top+"/"+$(e).width()+"/"+$(e).height());
	//[id:{top:,left:,width:,height:,cVertical:,cHorizl:}]
	
	var divPosDetails = {}; //var divPosDetails = new Object();
	
	divPosDetails.top = $(e).position().top;
	divPosDetails.left = $(e).position().left;
	divPosDetails.width = $(e).width();
	divPosDetails.height = $(e).height();
	divPosDetails.cv = $(e).position().top;
	divPosDetails.ch = $(e).position().left;
	
	divPos[e.id] = divPosDetails;
	
	//$(this).css({"top":$(e).position().top,"left":$(e).position().left});
	
});

console.log("divPos",divPos);

for(var a in divPos){
	console.log("a",a);
}

var dropPos;
*/

$(function(){
	
	$("#list").jqGrid({
		url			:	"${context_path}/bsc/base/strategy/strategy/strategyList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
							{name:"year",			index:"year",				hidden:true},
							{name:"strategyId",		index:"strategyId",			hidden:true},
							{name:"strategyNm",		index:"strategyNm",			width:100,	align:"left",		label:"<spring:message code="word.strategyNm"/>",
								editable:true, edittype:"text", editrules:{custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:150}
							},
							{name:"perspectiveId",	index:"perspectiveId",		width:100,	align:"left",		label:"<spring:message code="word.perspectiveId"/>",
								editable:true, edittype:"select", formatter:'select', editrules:{required:true}, editoptions:{value:getPerspectiveSelect()}	
							},
							{name:"upStrategyId",	index:"upStrategyId",		width:100,	align:"left",		label:"<spring:message code="word.upStrategyNm"/>",
								editable:true, edittype:"select", formatter:'select', editoptions:{value:getUpStrategySelect()}
							},
							{name:"useYn",			index:"useYn",				width:100,	align:"center",		label:"<spring:message code="word.useYn"/>",
								editable:true, edittype:"select", formatter:'select', editrules:{required:true}, editoptions:{value:getUseYnSelect()}
							},
							{name:"sortOrder",	    index:"sortOrder",			width:50,	align:"center",		label:"<spring:message code="word.sortOrder"/>",
								editable:true, edittype:"text", editrules:{integer:true}, editoptions:{maxlength:3}
							}
						],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		multiselect	: true,
		cellEdit	: false,
		cellsubmit  :'clientArray'
	});
	
	
	
	$(".drag").resizable({
		ghost:true,
		//containment: "parent",
		//alsoResize:".dropCanvas",
		resize:function(e,ui){
			
		},
		stop:function(e,ui){
			
			//console.log($(e.target).closest(".dropCanvas"));
			//console.log(ui.size.width+":"+ui.size.height);
			
			var $parent = $(e.target).closest(".dropCanvas");
			
			//console.log("$parent",$parent);
			
			$parent.width(ui.size.width);
			$parent.height(ui.size.height);
			console.log($(e.target).closest(".dropCanvas:first").width()+":"+$(e.target).closest(".dropCanvas:first").height());
			
		}
	});
	
	
	$(".drag").draggable({
					//helper: "clone",
					stack : ".drag", //드레그하는 대상의 z-index 높게 설정
					cursor : "pointer", //마우스 커서
					revert: false, //원위치
					//revertDuration : 500, //윈위치 행위시간
					refreshPositions: true, //????
					//containment : '#big', // 부모요소 안에 종속
					start : function(e,ui){
						//pos = $(e.target).position();
						//console.log(pos);
						//console.log("ui",ui);
					},
					drag : function(e,ui){
			            //var pos = $(this).position(); // 드래그 하는 이미지의 위치값 알아내기
			            //console.log(ui);
			            //console.log(e);
			            //드레그 했을때 하위 컴포넌트 객체를 알아야 함.
			            //그리고 드레그 대상의 자리로 넣어야 함.
			            
			            //console.log(pos);
			            /*
			            for(var a in divPos){
										            	
			            	console.log("Number==="+Number(divPos[a].ch));
			            	console.log("Number==="+Number(divPos[a].cv));
			            	
			            	if(Math.floor(divPos[a].ch) +10 < pos.left && Math.floor(divPos[a].ch)+50 > pos.left &&
			            			Math.floor(divPos[a].cv)+10 < pos.top && Math.floor(divPos[a].cv)+50 > pos.top &&
			            			a != $(e.target).attr("id")){
			            		console.log("a="+a);
			            		
			            		console.log("id="+$(e.target).attr("id"));
			            		
	            				console.log("e top",divPos[$(e.target).attr("id")].top);
	            				console.log("a top",divPos[a].top);
	            				console.log("e left",divPos[$(e.target).attr("id")].left);
	            				console.log("a left",divPos[a].left);
	            				
	            				var et=0;
	            				var el=0;
	            				var at=0;
	            				var al=0;
	            				
	            				$("#"+a).animate({ top : divPos[$(e.target).attr("id")].top-divPos[a].top, left : divPos[$(e.target).attr("id")].left-divPos[a].left}, 500, 'easeOutBack' ); // 커지게 만들고 원위치로
	            				$("#"+$(e.target).attr("id")).animate({ top : divPos[a].top-divPos[$(e.target).attr("id")].top, left : divPos[a].left-divPos[$(e.target).attr("id")].left}, 500, 'easeOutBack' ); // 커지게 만들고 원위치로
	            				
	            				var bleft = divPos[a].left;
	            				var btop = divPos[a].top;
	            				var bcv = divPos[a].cv;
	            				var bch = divPos[a].ch;
	            				
	            				divPos[a].left=divPos[$(e.target).attr("id")].left;
	            				divPos[a].top=divPos[$(e.target).attr("id")].top;
	            				divPos[a].cv=divPos[$(e.target).attr("id")].cv;
	            				divPos[a].ch=divPos[$(e.target).attr("id")].ch;
	            				
	            				divPos[$(e.target).attr("id")].left = bleft;
	            				divPos[$(e.target).attr("id")].top = btop;
	            				divPos[$(e.target).attr("id")].cv = bcv;
	            				divPos[$(e.target).attr("id")].ch = bch;
	            				
	            				console.log("divPos",divPos);
	            				
	            				et = $("#"+$(e.target).attr("id")).css("top");
	            				el = $("#"+$(e.target).attr("id")).css("left");
	            				at = $("#"+a).css("top");
	            				al = $("#"+a).css("left");
	            				
	            				console.log(et+"/"+el+"/"+at+"/"+al+"/");
	            				
	            				break;
			            	}
			            }
			            */
			            
			            //$( '.drag' ).not( this ).css({ top : pos.top, left : pos.left });
			            return true;
			        },
					stop : function(){ // 드래그 종료시 실행
				    	$(".drag").css({"top":0,"left":0});
					}	
			  });
	
	
	$(".dropCanvas").droppable({
		drop:function(e,ui){
			
			//console.log("ui",ui);
			
			//이동대상box(drag)
			var $dragBox = $(ui.draggable.context);
			//변경대상 box
			var $replaceBox = $(this).find("div:first");
			//이동대상 canvas
			var $dragCan = $("#d"+ui.draggable[0].id);
			//변경대상 canvas
			var $replaceCan = $(this);
			
			
			console.log("$dragBox====",$dragBox.length);
			console.log("$replaceBox====",$replaceBox.length);
			console.log("$dragCan====",$dragCan.length);
			console.log("$replaceCan====",$replaceCan.length);
			console.log("$replaceCan====",$replaceCan.context);
			
			console.log("$dragBox====",$dragBox.attr("id"));
			console.log("$replaceBox====",$replaceBox.attr("id"));
			console.log("$dragCan====",$dragCan.attr("id"));
			console.log("$replaceCan====",$replaceCan.attr("id"));
			
		
			//console.log("$this====",$(this).find("div:first").attr("id"));
			//console.log("$target====",$(e.target).find("div:first").attr("id"));
				
			if($dragBox.attr("id") != undefined && $replaceBox.attr("id") != undefined &&
					$dragCan.attr("id") != undefined && $replaceCan.attr("id") != undefined){
				
				var replaceWidth = $replaceBox.width();
				var replaceHeight = $replaceBox.height();
				var dragWidth = $dragBox.width();
				var dragHeight = $dragBox.height();
				
				
				$replaceBox.width(replaceWidth);
				$replaceBox.height(replaceHeight);
				$replaceCan.width(dragWidth);
				$replaceCan.height(dragHeight);
				$dragBox.width(dragWidth);
				$dragBox.height(dragHeight);
				$dragCan.width(replaceWidth);
				$dragCan.height(replaceHeight);
				
				$replaceBox.appendTo($dragCan);
				$dragBox.appendTo($replaceCan);
				
				//$(ui.draggable.context).appendTo(this);
				//$(this).attr("id","d"+$(ui.draggable.context).attr("id"));
				$replaceCan.attr("id","d"+$dragBox.attr("id"));
				$dragCan.attr("id","d"+$replaceBox.attr("id"));
			}else{
				//윈위지로 가세요~~~~
				$(".drag").draggable( "option", "revert", true );
			}
			//alert("drop!!!!!!!!!!!");
		}
	});
	
	
	//$(".dragGroup").sortable();
	/*
	$(".dragGroup").sortable({
		disableSelection : true	
	});
	*/

});
	
	/*리사이즈 처리*/
	
</script>
	
<form:form commandName="searchVO" id="f" name="f" method="post">
	
	<h1>Drag the blue boxes into the red box and back</h1>
	<div id="big" class="dragGroup">
		<div id="dboxA" class="dropCanvas">	
			<div class="drag" id="boxA" >
				<table id="list"></table>
			</div>
		</div>	
		<div id="dboxB" class="dropCanvas">
			<div class="drag" id="boxB" >
			</div>
		</div>
		<div id="dboxC" class="dropCanvas">
			<div class="drag" id="boxC" >
			</div>
		</div>
		<div id="dboxD" class="dropCanvas">
			<div class="drag" id="boxD" >
			</div>
		</div>
	</div>
</form:form>
