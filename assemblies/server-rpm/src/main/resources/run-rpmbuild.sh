#!/bin/sh

export RPM_TOPDIR=$PWD

mkdir -p $RPM_TOPDIR/BUILD
mkdir -p $RPM_TOPDIR/RPMS
cp ~/jboss-*.zip $RPM_TOPDIR/SOURCES

rpmbuild --define "_topdir $RPM_TOPDIR" -bb $RPM_TOPDIR/SPECS//time4u-server.spec
rpmbuild --define "_topdir $RPM_TOPDIR" -bb $RPM_TOPDIR/SPECS//time4u-server-mysql.spec
rpmbuild --define "_topdir $RPM_TOPDIR" -bb $RPM_TOPDIR/SPECS//time4u-server-postgres.spec
rpmbuild --define "_topdir $RPM_TOPDIR" -bb $RPM_TOPDIR/SPECS//time4u-server-apache.spec
