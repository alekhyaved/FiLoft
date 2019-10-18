<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
<h1>Login</h1>
<form action="/loginUser" method="POST">
<div class="login">
<label for="emailid">Email ID</label>
<input type="text" name="emailid" placeholder="Email ID" />
<label for="password">Password</label>
<input type="password" name="password" placeholder="Password" />
<button type="submit">Login</button>
</div>
</form>
</body>
</html>