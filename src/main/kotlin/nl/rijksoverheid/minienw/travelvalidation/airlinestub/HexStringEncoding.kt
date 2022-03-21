package nl.rijksoverheid.minienw.travelvalidation.airlinestub

class HexStringEncoding {
    companion object{
        fun encode(bytes: ByteArray) :String
        {
            val result = StringBuilder();
            for (i in bytes) {
                result.append(String.format("%02x", i));
            }
            return result.toString();
        }

        fun decode(hexString: String): ByteArray {
            if (hexString.length == 0 || hexString.length % 2 != 0)
                throw NumberFormatException("Incorrect length.")

            var result = ByteArray(hexString.length / 2)

            var i = 0
            while (i < hexString.length)
            {
                result[i / 2] = hexToByte(hexString.substring(i, i + 2))
                i += 2
            }

            return result
        }
        private fun hexToByte(hexString: String): Byte {
            val firstDigit: Int = toDigit(hexString[0])
            val secondDigit: Int = toDigit(hexString[1])
            return ((firstDigit shl 4) + secondDigit).toByte()
        }

        private fun toDigit(hexChar: Char): Int = hexChar.digitToIntOrNull(16) ?: throw NumberFormatException()
}}