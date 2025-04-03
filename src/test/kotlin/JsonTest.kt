import com.eclipsesource.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class JsonTest {
    @Test
    fun verifyParsing() {
        val json = """
            {
                "status": "OK",
                "message": "",
                "dst": "1",
                "zoneStart": 1743296400,
                "zoneEnd": 1761440399,
                "timestamp": 1743360565
            }""".trimIndent()
        val obj = Json.parse(json).asObject()
        assertEquals(true, obj["dst"].asString().toInt() != 0)
        assertEquals(1761440399L, obj["zoneEnd"].asLong())
    }
}