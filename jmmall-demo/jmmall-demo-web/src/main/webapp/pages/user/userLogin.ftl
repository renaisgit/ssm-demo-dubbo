<html>
<head>
<@ui.commonMETA/>
<title>用户列表</title>
<@ui.commonCSS/>
<link rel="stylesheet" href="${jsRoot}/css/signin.css">
</head>
<body>
	<div class="container">
      <form class="form-signin" role="form">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="email" class="form-control" placeholder="Email address" required autofocus>
        <input type="password" class="form-control" placeholder="Password" required>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> Remember me
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>
    </div>
</body>
<@ui.commonJQUERY/>
</html>