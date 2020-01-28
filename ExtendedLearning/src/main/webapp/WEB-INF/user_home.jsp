<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Extended Learning</title>

<jsp:include page="shared_jsp/jsp_scripts/styleTop.jsp" />
</head>
<body>

	<header class="main-header">
		<jsp:include page="shared_jsp/header.jsp" />
	</header>

	<article class="containergrid">


		<jsp:include page="shared_jsp/nav.jsp" />


		<div class="main-container">

			<c:if test="${profile != null}">
				<div class="row profile-info">
					<div class="card col-md-6">
						<img alt="image" src="${profile.image.imageUrl}" class="card-img-top">
						<div class="card-body">
							<h5>${profile.user.username }</h5>
							<form action="showUpdateAccount.do" method="POST">
								<button type="submit" class="btn btn-info">Update Account</button>
							</form>
						</div>
					</div>

					<div class="card col-md-6">
					<div class="card-body">
						<p>First Name: ${profile.firstName }</p>
						<p>Last Name: ${profile.lastName }</p>
						<p>Email: ${profile.email }</p>
						<p>Job Title: ${profile.jobTitle }</p>
						<p>Biography: ${profile.about }</p>
						</div>
					</div>
				</div>
				
				<div class="row profile-logs">
					<c:if test="${logs != null }">
					<div>
						<table class="table table-striped">
							<thead class="thead-light">
								<tr>
									<th scope="col">Topic Title</th>
									<th scope="col">Date visited</th>
								</tr>
							</thead>
								<tbody>
							<c:forEach var="log" items="${logs }">

									<tr>
										<td scope="row">${log.topic.title }</td>
										<td scope="row">viewed at: ${log.timeStamp }</td>
									</tr>
							</c:forEach>
								</tbody>
						</table>
					</div>
					</c:if>
				</div>
				
			</c:if>
		</div>



	</article>



	<footer>
		<jsp:include page="shared_jsp/footer.jsp" />
	</footer>


	<jsp:include page="shared_jsp/jsp_scripts/styleBot.jsp" />
</body>
</html>