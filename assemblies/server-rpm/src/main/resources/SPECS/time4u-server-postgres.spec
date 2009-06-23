Summary: Time4U server Postgres backend
Name: time4u-server-postgres
Version: ${time4u.version}
Release: ${buildNumber}
License: MIT
Group: Productivity/Networking/Web/Servers
Source: https://sourceforge.net/projects/time4u/
BuildArch: noarch
Requires: time4u-server postgresql-jdbc
BuildRoot: /tmp/time4u-postgres
%description
Use Postgres as database backend.

%prep
JBOSS_VERSION=5.0.1.GA
rm -rf jboss-$JBOSS_VERSION
rm -rf time4u-with-jboss
unzip $RPM_SOURCE_DIR/jboss-$JBOSS_VERSION-jdk6.zip
mv jboss-$JBOSS_VERSION time4u-with-jboss
tar xjf $RPM_SOURCE_DIR/time4u-assemblies-deploy.tar.bz2

%install
rm -rf $RPM_BUILD_ROOT/srv/time4u-with-jboss
mkdir -p $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/deploy/messaging
mkdir -p $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/conf
sed "s/@db.user@/time4u/ ; s/@db.name@/time4u/ ; s/@db.password@/time4u/ ; s/@db.host@/localhost/" $RPM_BUILD_DIR/time4u-assemblies-deploy-*/db/postgres/time4u-ds.xml > $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/deploy/time4u-ds.xml
mv $RPM_BUILD_DIR/time4u-assemblies-deploy-*/db/postgres/time4u-hibernate.cfg.xml $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/conf
cp $RPM_SOURCE_DIR/time4u-postgres-init.sql $RPM_BUILD_ROOT/srv/time4u-with-jboss
mkdir -p $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/deploy/messaging
sed "s/DefaultDS/Time4UDS/ ; 149d" $RPM_BUILD_DIR/time4u-with-jboss/docs/examples/jms/postgresql-persistence-service.xml > $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/deploy/messaging/postgresql-persistence-service.xml

%files
%defattr(-,wwwrun,www)
/srv/time4u-with-jboss/server/time4u/deploy/time4u-ds.xml
/srv/time4u-with-jboss/server/time4u/conf/time4u-hibernate.cfg.xml
/srv/time4u-with-jboss/time4u-postgres-init.sql
/srv/time4u-with-jboss/server/time4u/deploy/messaging/postgresql-persistence-service.xml

%post
ln -s /usr/share/pgsql/postgresql-*.jdbc3.jar /srv/time4u-with-jboss/server/time4u/lib
rm -f /srv/time4u-with-jboss/server/time4u/deploy/messaging/hsqldb-persistence-service.xml

