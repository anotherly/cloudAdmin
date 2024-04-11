<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>CLOUD 24 365 관리자 페이지</title>
<script>

	$(document).ready( function() {
		console.log("공지사항 상세");
		var tagId='${data.NOTICE_ID}';
		var sibal = '${login.AUTH_CODE}';
		//목록 버튼 클릭 시
		$("#btnList").click(function(){
			location.href="/admin/support/notice/noticeList.do"; 
		});
		//수정 버튼 클릭 시
		$("#btnUpdate").click(function(){
			 $("#work").load("/admin/support/notice/noticeUpdate.do",{"NOTICE_ID":tagId}); 
		});
		//삭제 버튼 클릭 시
		$("#btnDelete").click(function(){
			if(confirm("선택하신 항목을 삭제하시겠습니까?")==true){
				var idArr=[]; // 회원 id값 배열
				idArr.push(tagId);//배열에 아이디 값 삽입
//				//console.log("보낼 값 : "+ idArr);
				var url="/admin/support/notice/noticeDelete.ajax";
				var data = {"idArr":idArr};
				var callback= "/admin/support/notice/noticeList.do";
				ajaxMethod(url, data, callback);
    		}
		});//btnDelte
	});
	
</script>
</head>
                <!-- contents_box Start -->
                <div id="contents_box" class="contents_box">
                    <!-- 컨텐츠 테이블 헤더 Start -->
                    <div class="ctn_tbl_header">
                        <div class="ttl_ctn">공지사항 상세</div><!-- 컨텐츠 타이틀 -->
                    </div>
                    <!-- 컨텐츠 테이블 헤더 End -->
                    <!-- 컨텐츠 테이블 영역 Start -->
                    <form name="insertForm" id="acDetailFrm" method="post" enctype="multipart/form-data">
                    <div class="ctn_tbl_area">
					
                        <div class="ctn_tbl_row">
                            <div class="ctn_tbl_th fm_rep">분류</div>
                            <div class="ctn_tbl_td">
 								${data.NOTICE_TYPE_NM}
                            </div>
                            <div class="ctn_tbl_th">추가 발송</div>
                            <div class="ctn_tbl_td">
								<label class="fm_checkbox">
									<span class="langSpan">이메일</span>
									<input type="checkbox" name="FLAG_EMAIL" id="chk_status0" value="Y"
										<c:if test="${data.FLAG_EMAIL eq 'Y'}">checked</c:if>
									disabled>
									<span class="checkmark"></span>
								</label>
								
								<label class="fm_checkbox">
									<span class="langSpan">SMS</span>
									<input type="checkbox" name="FLAG_SMS" id="chk_status1" value="Y"
										<c:if test="${data.FLAG_SMS eq 'Y'}">checked</c:if>									
									disabled>
									<span class="checkmark"></span>
								</label>
                            </div>
                        </div>
						<div class="ctn_tbl_row">
                            <div class="ctn_tbl_th fm_rep">제목</div>
                            <div class="ctn_tbl_td">
                                ${data.NOTICE_TITLE}
                            </div>
                        </div>
                        
						<div class="ctn_tbl_row">
                            <div class="ctn_tbl_th">파일첨부</div>
                            <div class="ctn_tbl_td">
	                            <c:forEach var="fvo" items="${fileList}">
									<a href="/download.ajax?FILE_ID=${fvo.FILE_ID}">${fvo.FILE_NAME}</a>
							    </c:forEach>
                            </div>
                        </div>
                                                
                        <div class="ctn_tbl_row">
                            <div class="ctn_tbl_th">내용</div>
                            <div class="ctn_tbl_td">
                                <textarea id="CONTENT" name="CONTENT" class="long-cont" style="height:470px;" readonly>
                                ${data.CONTENT}
                                </textarea>
                            </div>
                        </div>
                    </div>
                    </form>
                </div>
                <!-- contents_box End -->
                
                <!-- footer Start ------------------>
                <div id="footer" class="footer-wrap">
                    <div id="footer-inner" class="footer-inner">
                        <c:if test="${fn:length(login.AUTH_CODE)>2}">
	                        <!-- btn_box Start -->
	                        <div class="btn_box">
				               	 <div class="right">
									<button  id="btnList"  class="btn">
										<span class="langSpan">목록</span>
									</button>
									<button  id="btnUpdate"  class="btn">
										<span class="langSpan">수정</span>
									</button>
									<button  id="btnDelete"  class="btn">
										<span class="langSpan">삭제</span>
									</button>
								</div>
	                        </div>
	                        <!-- btn_box End -->
                        </c:if>
                    </div>
                </div>
                <!-- footer End ------------------>
</html>