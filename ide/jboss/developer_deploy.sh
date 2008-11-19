#!/bin/sh

if [ "$JBOSS_HOME" = "" ]
then
  echo "JBOSS_HOME not set"
  exit 1
fi

if [ "$JBOSS_CONF" = "" ]
then
  echo "JBOSS_CONF not set"
  exit 1
fi

CURRENT=`pwd` 

cd $JBOSS_HOME/server/$JBOSS_CONF/deploy
rm -rf time4u-ear.ear
mkdir time4u-ear.ear
cd time4u-ear.ear


jar xf $CURRENT/../../assemblies/server-ear/target/time4u-assemblies-ear-*.ear

WEB_UI=`ls time4u-server-web-ui-*.war`

rm -rf $WEB_UI
ln -s $CURRENT/../../server/web/ui/target/${WEB_UI%.war} $WEB_UI

WEB_WS=`ls time4u-server-web-ws-*.war`

rm -rf $WEB_WS
ln -s $CURRENT/../../server/web/ws/target/${WEB_WS%.war} $WEB_WS

ENTITIES=`ls time4u-server-entities-*.jar`

rm -rf $ENTITIES
ln -s $CURRENT/../../server/entities/target/$ENTITIES $ENTITIES

EJB=`ls time4u-server-ejb-*.jar`

rm -rf $EJB
ln -s $CURRENT/../../server/ejb/target/$EJB $EJB

JAAS=`ls time4u-server-jaas-*.sar`

rm -rf $JAAS
ln -s $CURRENT/../../server/jaas/target/$JAAS $JAAS

cd lib

API=`ls time4u-server-api-*.jar`

rm -rf $API
ln -s $CURRENT/../../server/api/target/$API $API

cd $CURRENT
