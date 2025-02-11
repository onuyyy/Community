docker run -d \
--name community-mysql \
-e MYSQL_ROOT_PASSWORD="1" \
-e MYSQL_USER="root" \
-e MYSQL_PASSWORD="1" \
-e MYSQL_DATABASE="community-mysql" \
-p 3306:3306 \
mysql:latest
