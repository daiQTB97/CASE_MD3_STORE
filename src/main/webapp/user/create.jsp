
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Dashboard 1 | Velonic - Responsive Admin Dashboard Template</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta content="Responsive bootstrap 4 admin template" name="description">
    <meta content="Coderthemes" name="author">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- App favicon -->
    <link rel="shortcut icon" href="/assets/images/favicon.ico">

    <!-- Plugins css-->
    <link href="/assets/libs/sweetalert2/sweetalert2.min.css" rel="stylesheet" type="text/css">

    <!-- App css -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
    <link href="/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" id="bootstrap-stylesheet">
    <link href="/assets/css/icons.min.css" rel="stylesheet" type="text/css">
    <link href="/assets/css/app.min.css" rel="stylesheet" type="text/css" id="app-stylesheet">
    <link href="/assets/css/css-my-style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <!-- Topbar Start -->
    <%@include file="../shared/navbar-custom.jsp"%>
    <!-- end Topbar -->

    <!-- ========== Left Sidebar Start ========== -->
    <%@include file="../shared/left-side-menu.jsp"%>
    <!-- Left Sidebar End -->

    <!-- ============================================================== -->
    <!-- Start Page Content here -->
    <!-- ============================================================== -->

    <div class="content-page">
        <div class="content">

            <!-- Start Content-->
            <div class="container-fluid">

                <!-- start page title -->
                <div class="row">
                    <div class="col-12">
                        <div class="page-title-box">
                            <div class="page-title-right">
                                <ol class="breadcrumb m-0">
                                    <li class="breadcrumb-item"><a href="javascript: void(0);">Upvex</a></li>
                                    <li class="breadcrumb-item"><a href="javascript: void(0);">Dashboards</a></li>
                                    <li class="breadcrumb-item active">Dashboard</li>
                                </ol>
                            </div>
                            <h4 class="page-title">Well Come Create User</h4>
                        </div>
                    </div>
                </div>
                <!-- end page title -->
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <a class="btn btn-primary waves-effect waves-light" href="/users" style="width: 200px;">List User</a>
                                    </div>
                                    <div class="col-lg-6">

                                    </div>
                                </div>
                                <form method="post">
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="inputEmail4" class="col-form-label">Email</label>
                                            <input type="email" name="email" required class="form-control" id="inputEmail4" placeholder="Email">
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label for="inputPassword4" class="col-form-label">Password</label>
                                            <div class="input-group mb-3">
                                                <input type="password" name="password" required class="form-control" id="inputPassword4" placeholder="Password">
                                                <div class="input-group-append">
                                                    <button class="btn btn-outline-secondary" type="button" id="makeNone" onclick="makeNone()">
                                                        <span class="fa-solid fa-eye"></span>
                                                    </button>
                                                    <button class="btn btn-outline-secondary d-none" type="button" onclick="makeDNone()" id="makeDNone">
                                                        <span  class="fa-solid fa-eye-slash"></span>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="FullName" class="col-form-label">Full Name</label>
                                            <input type="text" name="fullname" required class="form-control" id="FullName" placeholder="Full Name">
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label for="Mobile" class="col-form-label">Mobile</label>
                                            <input type="text" name="mobile" required class="form-control" id="Mobile" placeholder="Mobile">
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="Address" class="col-form-label">Address</label>
                                            <input type="text" name="address" required class="form-control" id="Address" placeholder="Address">
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label for="inputState" class="col-form-label">Role</label>
                                            <select id="inputState" name="role" class="form-control">
                                                <option value="1">Admin</option>
                                                <option value="0" >User</option>
                                            </select>
                                        </div>

                                    </div>
                                    <button type="submit" class="btn btn-primary waves-effect waves-light">Create</button>

                                </form>
                                <h6>
                                    <c:if test="${requestScope['success'] == true}">
                                        <c:out value="${'Thêm người dùng thành công'}"></c:out>
                                    </c:if>
                                </h6>
                                <ul>
                                    <c:forEach items="${requestScope['errors']}" var="err">
                                        <li>${err}</li>
                                    </c:forEach>
                                </ul>

                            </div> <!-- end card-body -->
                        </div> <!-- end card-->
                    </div> <!-- end col -->
                </div>
            </div>  <!-- container -->
        </div> <!-- content -->
        <%@include file="../shared/footer.jsp"%>
    </div>
    <!-- ============================================================== -->
    <!-- End Page content -->
    <!-- ============================================================== -->
</div>

<%@include file="../shared/right-bar.jsp"%>
<div class="rightbar-overlay"></div>
<%@include file="../shared/script.jsp"%>

<script>
    const ipnElement = document.querySelector('#inputPassword4')
    const btnElement = document.querySelector('#makeNone')
    const btnElementNone = document.querySelector('#makeDNone')



    btnElement.addEventListener('click', function (){
        const currentType = ipnElement.getAttribute('type')
        ipnElement.setAttribute( 'type',currentType === 'password' ? 'text' : 'password')
        btnElement.classList.add('d-none')
        btnElementNone.classList.remove('d-none')
    })
    btnElementNone.addEventListener('click', function (){
        const currentType = ipnElement.getAttribute('type')
        ipnElement.setAttribute( 'type',currentType === 'text' ? 'password' : 'text')
        btnElement.classList.remove('d-none')
        btnElementNone.classList.add('d-none')
    })

</script>

</body>
</html>
