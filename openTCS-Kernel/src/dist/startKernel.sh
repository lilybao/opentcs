#!/bin/sh
#
# Start the openTCS kernel.
#

# Set base directory names.
export OPENTCS_BASE=.
export OPENTCS_HOME=.
export OPENTCS_CONFIGDIR="${OPENTCS_HOME}/config"
export OPENTCS_LIBDIR="${OPENTCS_BASE}/lib"

# Set the class path
export OPENTCS_CP="${OPENTCS_LIBDIR}/*"
export OPENTCS_CP="${OPENTCS_CP}:${OPENTCS_LIBDIR}/openTCS-extensions/*"

if [ -n "${OPENTCS_JAVAVM}" ]; then
    export JAVA="${OPENTCS_JAVAVM}"
else
    # XXX Be a bit more clever to find out the name of the JVM runtime.
    export JAVA="java"
fi

# Start kernel
${JAVA} -enableassertions \
    -Dopentcs.base="${OPENTCS_BASE}" \
    -Dopentcs.home="${OPENTCS_HOME}" \
    -Djava.util.logging.config.file=${OPENTCS_CONFIGDIR}/logging.config \
    -Djava.security.policy=file:${OPENTCS_CONFIGDIR}/java.policy \
    -Dorg.opentcs.util.configuration.xml.file="${OPENTCS_CONFIGDIR}/openTCS-config.xml" \
    -Dorg.opentcs.util.configuration.saveonexit=true \
    -Dorg.opentcs.virtualvehicle.profiles.file="${OPENTCS_CONFIGDIR}/virtualvehicle-profiles.xml" \
    -XX:-OmitStackTraceInFastThrow \
    -classpath "${OPENTCS_CP}" \
    -splash:bin/splash-image.gif \
    org.opentcs.kernel.RunKernel
