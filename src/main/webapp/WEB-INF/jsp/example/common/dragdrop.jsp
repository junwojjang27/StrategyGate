<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>

<style type="text/css">

.drag {
  width: 25%;
  height: 300px;
  background: blue;
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
  height: 600px;
  background: white;
  padding: 20px 20px 20px 20px;
}
section {
width: 415px;
  height: 200px;
  background:gray;
  margin: 20px auto;
}

</style>

<script type="text/javascript">
	function dragStart(ev) {
	   var target = ev.target;
	   var id = ev.target.getAttribute('id');
	   ev.dataTransfer.effectAllowed='copyMove';
	   ev.dataTransfer.setData("id", ev.target.getAttribute('id'));
	   //ev.dataTransfer.setDragImage(ev.target,250,200);
	   return true;
	}
	
	function dragEnter(ev) {
	   //console.log(ev.target);
	   //console.log(ev.target.clientX);
	   ev.preventDefault();
	   return true;
	}
	
	function dragOver(ev) {
	    ev.preventDefault();
	}
	
	function dragDrop(ev) {
	   var data = ev.dataTransfer.getData("id");
	   //alert(data);
	   console.log("data",data);
	   console.log("event",ev);
	   console.log("target",ev.target);
	   /*위치 바꾸는것으로 수정해야 함*/
	   
	   //var datas = data.split("&");
	   
	   var $fromId = "";
	   var $toId = "";
	   var $fromId = $("#"+data);
	   var $toId = $("#"+ev.target.getAttribute("id"));
	   
	   /*
	   var fromLeft = null;
	   var fromTop = null;
	   var toLeft = null;
	   var toTop = null;
	   
	   var $fromId = $("#"+data);
	   var $toId = $("#"+ev.target.getAttribute("id"));
	   var fromLeft = $fromId.position().left;
	   var fromTop = $fromId.position().top;
	   var toLeft = $toId.position().left;
	   var toTop = $toId.position().top;
	   */
	   //console.log("$fromId",$fromId);
	   //console.log("$toId",$toId);
	   
	    //console.log(fromLeft);
 	  	//console.log(fromTop);
 	  	//console.log(toLeft);
 	  	//console.log(toTop);
	   
	   if(ev.target.getAttribute("id") != 'big'){
		   	//document.getElementById("big").appendChild(document.getElementById(data));
	  	   	/*자리바꿈 처리*/
	  	   	//$fromId.css({"position":"absolute","top":toTop+"px","left":toLeft+"px"});
	  	  	//$toId.css({"position":"absolute","top":fromTop+"px","left":fromLeft+"px"});
	  	    //$fromId.resizable({ ghost: true });
	  	    //$toId.resizable({ ghost: true });
	  	    
	  	    $fromId.resizable({ ghost: true });
	  	    $toId.resizable({ ghost: true });
	  	    
	  	  	//console.log("$toId.next()",$toId.next());
	  	    
	  	    var fromAddId = $toId.next().attr("id");
	  	    var toAddId = $fromId.next().attr("id");
	  	    var fromAddId2 = $toId.prev().attr("id");
	  	    var toAddId2 = $fromId.prev().attr("id");
	  	  	
	  	    //console.log("fromAddId",fromAddId);
	  	    //console.log("toAddId",toAddId);
	  	    //console.log("fromAddId2",fromAddId2);
	  	    //console.log("toAddId2",toAddId2);
	  	    
	  	    var fromChkVal = "";
	  	    var toChkVal = "";
	  	    if(fromAddId != null && fromAddId != undefined){
	  	    	fromChkVal = fromAddId;
	  	    }else{
	  	    	fromChkVal = fromAddId2;
	  	    }
	  	    
	  	    if(toAddId != null && toAddId != undefined){
	  	    	toChkVal = toAddId;
	  	    }else{
	  	    	toChkVal = toAddId2;
	  	    }
	  	    
	  	    if(fromChkVal == $fromId.attr("id") && toChkVal == $toId.attr("id")){
	  	    	console.log("in");
	  	    	if(fromAddId != undefined && fromAddId2 != undefined){fromAddId = null;}
	  	    	if(toAddId != undefined && toAddId2 != undefined){toAddId = null;}
	  	    }
	  	    
	  	    console.log(fromAddId);
	  	    console.log(toAddId);
	  	    
	  	    if(fromAddId != null && fromAddId != undefined){
	  	    	$fromId.insertBefore("#"+fromAddId);
	  	    }else{
	  	    	$fromId.insertAfter("#"+fromAddId2);
	  	    }
	  	    
	  	    if(toAddId != null && toAddId != undefined){
	  	    	$toId.insertBefore("#"+toAddId);
	  	    }else{
	  	    	//console.log($("#"+toAddId2).attr("id"));
	  	    	$toId.insertAfter("#"+toAddId2);
	  	    }
	  	    
	  	    
	  	    //$fromId.replaceWith($toId);
	  	    //$toId.replaceWith($fromId);
	   }else{
		   //document.getElementById("big").appendChild(document.getElementById(data));
	   }
	   
	   //document.getElementById("big").appendChild(document.getElementById(datas[0]));
	   //ev.target.appendChild(document.getElementById(data));
	   ev.stopPropagation();
	   return false;
	}
	
	/*리사이즈 처리*/
	
</script>
	
<form:form commandName="searchVO" id="f" name="f" method="post">
	
	<%--
	<h1>Drag the blue boxes into the red box and back</h1>
	<div id="big" ondragenter="return dragEnter(event)"
	     ondrop="return dragDrop(event)" 
	     ondragover="return dragOver(event)"></div>
	<section id="section"  ondragenter="return dragEnter(event)" 
	     ondrop="return dragDrop(event)" 
	     ondragover="return dragOver(event)">
	<div class="drag" id="boxA" draggable="true" ondragstart="return dragStart(event)"></div>
	<div class="drag" id="boxB" draggable="true" ondragstart="return dragStart(event)"></div>
	</section>
	--%>
	
	<h1>Drag the blue boxes into the red box and back</h1>
	<div id="big" ondragenter="return dragEnter(event)"
	     ondrop="return dragDrop(event)" 
	     ondragover="return dragOver(event)">
		<div class="drag" id="boxA" draggable="true" ondragstart="return dragStart(event)"> <font size="20">A</font>
		</div>
		<div class="drag" id="boxB" draggable="true" ondragstart="return dragStart(event)"> <font size="20">B</font>
		</div>
		<div class="drag" id="boxC" draggable="true" ondragstart="return dragStart(event)"> <font size="20">C</font>
		</div>
		<div class="drag" id="boxD" draggable="true" ondragstart="return dragStart(event)"> <font size="20">D</font>
		</div>
	</div>
</form:form>
