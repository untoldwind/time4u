Summary: Time4U server
Name: time4u-server
Version: 0.9.8
Release: 1
License: MIT
Group: Productivity/Networking/Web/Servers
Source: https://sourceforge.net/projects/time4u/
BuildArch: noarch
Requires: java-1_6_0-sun-devel 
%description
This contains everything to run a basic Time4U server.

%prep
JBOSS_VERSION=5.0.1.GA
rm -rf jboss-$JBOSS_VERSION
rm -rf time4u-with-jboss
rm -rf time4u-assemblies-deploy-%{version}
unzip $RPM_SOURCE_DIR/jboss-$JBOSS_VERSION-jdk6.zip
mv jboss-$JBOSS_VERSION time4u-with-jboss
rm -rf time4u-with-jboss/server/all
rm -rf time4u-with-jboss/server/web
rm -rf time4u-with-jboss/server/minimal
rm -rf time4u-with-jboss/server/standard
rm -f time4u-with-jboss/bin/*.exe
rm -f time4u-with-jboss/bin/*.bat
mv time4u-with-jboss/server/default time4u-with-jboss/server/time4u
rm -rf time4u-with-jboss/docs
tar xjf $RPM_SOURCE_DIR/time4u-assemblies-deploy-%{version}.tar.bz2
mv time4u-assemblies-deploy-%{version}/ear/time4u-assemblies-ear.ear time4u-with-jboss/server/time4u/deploy
mv time4u-assemblies-deploy-%{version}/run-time4u.conf time4u-with-jboss/bin
mkdir time4u-with-jboss/log
touch time4u-with-jboss/log/jboss.log
chown wwwrun:www -R time4u-with-jboss

%install
mkdir -p /srv
rm -rf /srv/time4u-with-jboss
mv $RPM_BUILD_DIR/time4u-with-jboss /srv

sed 's/\/opt\/jboss/\/srv\/time4u-with-jboss/ ; s/\/usr\/java\/j2sdk1.4.1\/bin/\/etc\/alternatives\/java_sdk_1.6.0\/bin/ ; s/run.sh -c default/run.sh -c time4u -b 0.0.0.0/ ; s/su - $JBOSSUS -c/su - -s \/bin\/sh $JBOSSUS -c/ ; s/JBOSSUS:-"jboss"/JBOSSUS:-"wwwrun"/'  /srv/time4u-with-jboss/bin/jboss_init_suse.sh >/etc/init.d/time4u
chmod a+x /etc/init.d/time4u

%files
%defattr(-,wwwrun,www)
/srv/time4u-with-jboss
%attr(755,root,root)
/etc/init.d/time4u

