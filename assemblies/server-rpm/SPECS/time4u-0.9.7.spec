Summary: Time4U server
Name: time4u-server
Version: 0.9.7
Release: 1
License: MIT
Group: Productivity/Networking/Web/Servers
Source: https://sourceforge.net/projects/time4u/
BuildArch: noarch
Requires: apache2 apache2-mod_jk java-1_6_0-sun-devel 
%description
This contains everything to run a basic Time4U server.

%prep
rm -rf jboss-5.0.1.GA
rm -rf time4u-with-jboss
rm -rf time4u-assemblies-deploy-%{version}
unzip $RPM_SOURCE_DIR/jboss-5.0.1.GA-jdk6.zip
mv jboss-5.0.1.GA time4u-with-jboss
rm -rf time4u-with-jboss/server/all
rm -rf time4u-with-jboss/server/web
rm -rf time4u-with-jboss/server/minimal
rm -rf time4u-with-jboss/server/standard
mv time4u-with-jboss/server/default time4u-with-jboss/server/time4u
rm -rf time4u-with-jboss/client
rm -rf time4u-with-jboss/docs
tar xjf $RPM_SOURCE_DIR/time4u-assemblies-deploy-%{version}.tar.bz2
mv time4u-assemblies-deploy-%{version}/ear/time4u-assemblies-ear.ear time4u-with-jboss/server/time4u/deploy
mv time4u-assemblies-deploy-%{version}/run-time4u.conf time4u-with-jboss/bin
chown wwwrun:www -R time4u-with-jboss

%install
mkdir -p /srv
rm -rf /srv/time4u-with-jboss
mv $RPM_BUILD_DIR/time4u-with-jboss /srv

%files
%defattr(-,wwwrun,www)
/srv/time4u-with-jboss
