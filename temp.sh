curl -X POST \
  http://localhost:5000/uaa/oauth/token \
  -H 'Authorization: Basic YXBwbGljYXRpb246cGFzcw==' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'username=parrotbill@naver.com&password=admin&grant_type=password'
