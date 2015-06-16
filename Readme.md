##增加功能点：
* 二级菜单（选择城市页面分两级，第一级为省份，第二级为各省城市）  
* 背景图片切换（背景图片设置页面）  
* 城市自动定位（点击定位，根据当前地理位置刷新天气信息和城市图片）  
* 热门城市列表（在选择城市第一级页面可以快速选择热门城市）  
* 后台整点（6点，9点，12点，15点，18点，21点）推送天气信息  


##设置城市背景图片
http://fordawn.sinaapp.com/city/

##CI项目迁移到SAE：
1. config.php
```
$config['base_url']    = 'http://frozen.sinaapp.com/' ;
$config['index_page'] = '';
```

2. database.php
```
$db[ 'default']['hostname' ] = SAE_MYSQL_HOST_M;
$db[ 'default']['username' ] = SAE_MYSQL_USER;
$db[ 'default']['password' ] = SAE_MYSQL_PASS;
$db[ 'default']['database' ] = SAE_MYSQL_DB;
$db[ 'default']['dbdriver' ] = 'mysqli' ;
$db[ 'default']['dbprefix' ] = '' ;
$db[ 'default']['pconnect' ] = FALSE;
$db[ 'default']['db_debug' ] = TRUE;
$db[ 'default']['cache_on' ] = FALSE;
$db[ 'default']['cachedir' ] = '' ;
$db[ 'default']['char_set' ] = 'utf8' ;
$db[ 'default']['dbcollat' ] = 'utf8_general_ci' ;
$db[ 'default']['swap_pre' ] = '' ;
$db[ 'default']['autoinit' ] = TRUE;
$db[ 'default']['stricton' ] = FALSE;
$db[ 'default']['port' ] = SAE_MYSQL_PORT;
```
3. config.yaml加入
```
handle:
  - rewrite: if(!is_dir() && !is_file() && path~"/") goto "/index.php/%{QUERY_STRING}"
```

##CI去掉index.php
1. httpd.conf中加入LoadModule rewrite_module modules/mod_rewrite.so
2. httpd.conf中www目录设置  
 
     ```  
    Options FollowSymLinks  
    AllowOverride All
    Order allow,deny
    Allow from all
    ```
3. CI目录下与index.php同级建立.htaccess文件  

    ```  
    RewriteEngine on
    RewriteCond %{REQUEST_FILENAME} !-f
    RewriteCond $1 !^(index\.php|images|js|img|css|robots\.txt)
    RewriteRule ^(.*)$ index.php/$1 [L]
    ```  
4. config.php中  
    ```
    $config['index_page'] = '';
    ```

##SAE中CI去掉index.php
1. config.php中  
 
    ```
    $config['index_page'] = '';
    ```
2. config.yaml中

    ```
handle:
  - rewrite: if(!is_dir() && !is_file() && path~"/") goto "/index.php/%{QUERY_STRING}"
  ```