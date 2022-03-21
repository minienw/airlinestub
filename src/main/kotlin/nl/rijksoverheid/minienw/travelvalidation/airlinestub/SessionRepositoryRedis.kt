package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import com.google.gson.Gson
import nl.rijksoverheid.minienw.travelvalidation.service.services.SessionInfo
import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis

@Service
class SessionRepositoryRedis(val appSettings: IApplicationSettings, val idGenerator: ValidationServicesSubjectIdGenerator) :
    ISessionRepository {
    override fun save(sessionInfo: SessionInfo) {
        val session = createSession() //TODO settings
        try {
            var subjectId = idGenerator.next()
            while (session.get(subjectId) != null)
                subjectId = idGenerator.next()

            sessionInfo.vat.subject = subjectId
            val value = Gson().toJson(sessionInfo)
            session.set(sessionInfo.vat.subject, value);
            //TODO session.expire(sessionInfo.response.subjectId, 3600);
        } finally {
            session.client.close()
        }
    }

    private fun createSession(): Jedis {
        val session = Jedis(appSettings.redisHost, 6379)
        return session
    }

    override fun find(subject: String): SessionInfo? {
        val session = createSession() //TODO settings
        try {
            val value = session.get(subject)
            return if (value == null) null else Gson().fromJson(value, SessionInfo::class.java)
        } finally {
            session.client.close()
        }
    }

    override fun remove(subject: String) {
        val session = createSession() //TODO settings
        try {
        } finally {
            session.client.close()
        }
    }
}

