package nl.rijksoverheid.minienw.travelvalidation.airlinestub.swagger

import java.io.File

sealed class Examples
{
    companion object {
        const val Identity = "{\n" +
                "  \"id\": \"http://localhost:9001/identity\",\n" +
                "  \"verificationMethod\": [\n" +
                "    {\n" +
                "      \"id\" : \"http://localhost:9001/verificationmethod#AccessTokenSign-1\",\n" +
                "      \"type\": \"JsonWebKey2020\",\n" +
                "      \"controller\": \"http://localhost:9001/identity\",\n" +
                "      \"publicKeyJwk\": {\n" +
                "        \"x5c\":[\"MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAp4aod1QhVQIZ5cVW05qZUIOALxaFn+sVblneSxPKuvuaW3V/OTcm8sby8AyGYuhLsIkb65/afrbn5hC1E3RMcDJu9MJByvS0PReckuVgxJZE9FzAFJLpld5OScCSIwEabCyXOEi/324IfGnzNZYxBUa3Rb69ep1J3H7Ddp1VZXeaKPcAaKQ/nvCFle0TERRuXOysAt/m1i2hOSBKNE7KtyCRwFIn1dG7HcrpHOYzONTeQIEEw1kPjRszDU6EG7F7alMibar781ZNuHD1V/jRDDPILi9u5VHV1N42MDX+60SjOlBjY+7ncet+Yem0gGvnvcQI1f+BUyN0jVUppFbX7X59+pmQSonpfWnCiDWU32qsaMdue+GCi331vk79kKOSH9QlBWPXVL1GTFnoQjb/oTiMeKy1qsdeGN/x8GwQMCJG0xt2ncqguUdk6y9uaNxZboWpB9w+heZtc8YfqdiQJuM5HAM355kCgl0cUci+9hCQJeW/YcEAGSQPUW2lZbomRtoc41AcK6k0dIzD20Ej3rDxWxvqwOBz1TDsuGnGVcYXYQcnHbFJWfRK27l+yypg0gsIVWpND5y7W+Age6kj/GMDCM2LFGra30OjatF+SEt6Mtk0Wh0Gx1JIPPEc+9PYDQ5HsfNQO00HuGS7rHtMQUbWMqpRZGJgYCaQuSXLD7cCAwEAAQ==\"],\n" +
                "        \"kid\":\"L6TcjnbZge4=\",\n" +
                "        \"alg\":\"RS256\",\n" +
                "        \"use\":\"sig\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"service\": [\n" +
                "    {\n" +
                "      \"id\": \"http://localhost:9001/identity/service#AccessTokenService-99\",\n" +
                "      \"type\": \"AccessTokenService\",\n" +
                "      \"serviceEndpoint\": \"http://localhost:9001/token\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"http://localhost:9001/identity/service#ResultTokenService-0\",\n" +
                "      \"type\": \"ResultTokenService\",\n" +
                "      \"serviceEndpoint\": \"http://localhost:9001/callback\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"
    }
}
