package nl.rijksoverheid.minienw.travelvalidation.airlinestub.services

import com.google.gson.Gson
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.IApplicationSettings
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.ISessionRepository
import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis


@Service
class SessionRepositoryRedis(val appSettings: IApplicationSettings) : ISessionRepository {

    //HACK to distinguish keys the stub keys from the validation service keys during dev cos shared redis server
    val prefix:String = "stub:"

    override fun save(sessionInfo: SessionInfo) {
        val session = createSession() //TODO settings
        try {
            val value = Gson().toJson(sessionInfo)
            session.set("${prefix}${sessionInfo.subjectId}", value)
        } finally {
            session.client.close()
        }
    }

    private fun createSession(): Jedis {
        val session = Jedis(appSettings.redisHost, 6379)
        //session.auth("noneShallPass") //TODO settings
        return session
    }

    override fun find(subject: String): SessionInfo? {
        val session = createSession() //TODO settings
        try {
            val value = session.get("${prefix}${subject}")
            return if (value == null) null else Gson().fromJson(value, SessionInfo::class.java)
        } finally {
            session.client.close()
        }
    }

    override fun remove(subject: String) {
        val session = createSession() //TODO settings
        try {
            //No need to remove...
        } finally {
            session.client.close()
        }
    }
}

