Summary: Time4U server Apache connector
Name: time4u-server-apache
Version: ${time4u.version}
Release: ${buildNumber}
License: MIT
Group: Productivity/Networking/Web/Servers
Source: https://sourceforge.net/projects/time4u/
BuildArch: noarch
Requires: apache2 apache2-mod_jk time4u-server 
BuildRoot: /tmp/time4u-apache
%description
Time4U server apache2 connector.

%install
mkdir -p $RPM_BUILD_ROOT/etc/apache2/conf.d
cp $RPM_SOURCE_DIR/time4u.conf $RPM_BUILD_ROOT/etc/apache2/conf.d
cp $RPM_SOURCE_DIR/time4u-workers.properties $RPM_BUILD_ROOT/etc/apache2/conf.d

%files
%defattr(-,wwwrun,www)
/etc/apache2/conf.d/time4u.conf
/etc/apache2/conf.d/time4u-workers.properties

