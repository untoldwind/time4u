Summary: Time4U server Apache connector
Name: time4u-server-apache
Version: 0.9.8
Release: 1
License: MIT
Group: Productivity/Networking/Web/Servers
Source: https://sourceforge.net/projects/time4u/
BuildArch: noarch
Requires: apache2 apache2-mod_jk time4u-server 
%description
Time4U server apache2 connector.

%install
mkdir -p /etc/apache2/conf.d
cp $RPM_SOURCE_DIR/time4u.conf /etc/apache2/conf.d
cp $RPM_SOURCE_DIR/time4u-workers.properties /etc/apache2/conf.d

%files
/etc/apache2/conf.d/time4u.conf
/etc/apache2/conf.d/time4u-workers.properties

