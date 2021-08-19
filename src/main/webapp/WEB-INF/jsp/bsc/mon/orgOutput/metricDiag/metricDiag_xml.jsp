<?xml version="1.0" encoding="utf-8"?>
<%@ page contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<root>
   <attribute pdfVisible="true" pdfUrl="${context_path}/bsc/module/commModule/MatrixPdfDownload" applicationBar="true" position="horizontal">
			<node attributes="목표/false,실적/false,점수/false"  name="지표연계표" />
   </attribute>
<c:set value="0" var="idx"/>
<c:set value="${fn:length(list)}" var="listSize"/>
<c:set value="${fn:length(list1)}" var="listSize1"/>
<c:set value="${fn:length(list2)}" var="listSize2"/>
<c:set value="${fn:length(list3)}" var="listSize3"/>
<c:set value="${fn:length(list4)}" var="listSize4"/>
<c:set value="${fn:length(list5)}" var="listSize5"/>
<c:set value="${fn:length(list6)}" var="listSize6"/>
<c:set value="${fn:length(list7)}" var="listSize7"/>

<c:if test="${listSize != 0}">
	<c:if test="${listSize1 != 0}">
    	<c:forEach var="list1" items="${list1}" varStatus="list1Status">
    		<c:set value="${list1.scDeptNm}${list1.strategyNm}" var="title1" />
			<c:set value="${list1.scDeptId}|${list1.strategyId}|${list1.metricId}" var="url1"/>
			<node label="${list1.metricNm}"
				color="${sinalMap[list1.status]}" fontColor="0x000000" title="${title1}"
				attributes="목표/${list1.target},실적/${list1.actual},점수/${list1.score}"
				url="${url1}">
        	<c:if test="${listSize2 != 0}">
            	<c:forEach var="list2" items="${list2}" varStatus="list2Status">
					<c:if test="${list1.metricId eq list2.upMetricId}">
						<c:set value="${list2.scDeptNm}${list2.strategyNm}" var="title2"/>
    					<c:set value="${list2.scDeptId}|${list2.strategyId}|${list2.metricId}" var="url2"/>
						<node label="${list2.metricNm}"
							color="${sinalMap[list2.status]}" fontColor="0x000000" title="${title2}"
							attributes="목표/${list2.target},실적/${list2.actual},점수/${list2.score}"
							url="${url2}">
						<c:if test="${listSize3 != 0}">
			            	<c:forEach var="list3" items="${list3}" varStatus="list3Status">
								<c:if test="${list3.metricId eq list3.upMetricId}">
									<c:set value="${list3.scDeptNm}${list3.strategyNm}" var="title3"/>
			    					<c:set value="${list3.scDeptId}|${list3.strategyId}|${list3.metricId}" var="url3"/>
									<node label="${list3.metricNm}"
										color="${sinalMap[list3.status]}" fontColor="0x000000" title="${title3}"
										attributes="목표/${list3.target},실적/${list3.actual},점수/${list3.score}"
										url="${url3}">
									<c:if test="${listSize4 != 0}">
						            	<c:forEach var="list4" items="${list4}" varStatus="list4Status">
											<c:if test="${list4.metricId eq list4.upMetricId}">
												<c:set value="${list4.scDeptNm}${list4.strategyNm}" var="title4"/>
						    					<c:set value="${list4.scDeptId}|${list4.strategyId}|${list4.metricId}" var="url4"/>
												<node label="${list4.metricNm}"
													color="${sinalMap[list4.status]}" fontColor="0x000000" title="${title4}"
													attributes="목표/${list4.target},실적/${list4.actual},점수/${list4.score}"
													url="${url4}">
												<c:if test="${listSize5 != 0}">
									            	<c:forEach var="list5" items="${list5}" varStatus="list5Status">
														<c:if test="${list5.metricId eq list5.upMetricId}">
															<c:set value="${list5.scDeptNm}${list5.strategyNm}" var="title5"/>
									    					<c:set value="${list5.scDeptId}|${list5.strategyId}|${list5.metricId}" var="url5"/>
															<node label="${list5.metricNm}"
																color="${sinalMap[list5.status]}" fontColor="0x000000" title="${title5}"
																attributes="목표/${list5.target},실적/${list5.actual},점수/${list5.score}"
																url="${url5}">
															<c:if test="${listSize6 != 0}">
												            	<c:forEach var="list6" items="${list6}" varStatus="list6Status">
																	<c:if test="${list6.metricId eq list6.upMetricId}">
																		<c:set value="${list6.scDeptNm}${list6.strategyNm}" var="title6"/>
												    					<c:set value="${list6.scDeptId}|${list6.strategyId}|${list6.metricId}" var="url6"/>
																		<node label="${list6.metricNm}"
																			color="${sinalMap[list6.status]}" fontColor="0x000000" title="${title6}"
																			attributes="목표/${list6.target},실적/${list6.actual},점수/${list6.score}"
																			url="${url6}">
																		<c:if test="${listSize7 != 0}">
															            	<c:forEach var="list7" items="${list7}" varStatus="list7Status">
																				<c:if test="${list7.metricId eq list7.upMetricId}">
																					<c:set value="${list7.scDeptNm}${list7.strategyNm}" var="title7"/>
															    					<c:set value="${list7.scDeptId}|${list7.strategyId}|${list7.metricId}" var="url7"/>
																					<node label="${list7.metricNm}"
																						color="${sinalMap[list7.status]}" fontColor="0x000000" title="${title7}"
																						attributes="목표/${list7.target},실적/${list7.actual},점수/${list7.score}"
																						url="${url7}">
                                                            						</node>
                                                            					</c:if>
                                                            				</c:forEach>
                                                            			</c:if>
                                                						</node>
                                                					</c:if>
                                                				</c:forEach>
                                                			</c:if>
                                    						</node>
                                    					</c:if>
                                    				</c:forEach>
                                    			</c:if>
                        						</node>
                        					</c:if>
                        				</c:forEach>
                        			</c:if>
            						</node>
            					</c:if>
            				</c:forEach>
            			</c:if>
						</node>
					</c:if>
				</c:forEach>
			</c:if>
			</node>
		</c:forEach>
	</c:if>
</c:if>
</root>