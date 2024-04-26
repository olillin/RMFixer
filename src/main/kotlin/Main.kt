package com.olillin.rmfixer

import java.io.*

fun main(args: Array<String>) {
    val filepath = args[0]
    val input = DataInputStream(FileInputStream(filepath))

    val output = DataOutputStream(FileOutputStream("fixed.tmcpr"))
    val fixedTimestampsOutput = DataOutputStream(FileOutputStream("fixed_timestamps.txt"))

    fixRecording(input, output, fixedTimestampsOutput)
}

const val BYTES_IN_MEGABYTE: Int = 1048576

fun fixRecording(input: DataInputStream, output: DataOutputStream, fixedTimestampsOutput: DataOutputStream) {
    // Track progress
    var prevRemaining: Int = input.available() / 1024
    var readBytes: Int = 0

    var prevTimestamp: Int = 0
    var prevWrittenTimestamp: Int = 0
    try {
        while (true) {
            val timestamp: Int = input.readInt()
            val packetLength: Int = input.readInt()
            val packet: ByteArray = input.readNBytes(packetLength)

            // Progress tracker
            readBytes += 4 * 2 + packetLength
            val remaining: Int = input.available() / BYTES_IN_MEGABYTE
            if (remaining < prevRemaining) {
                print("\u001B[2K\rRemaining: $remaining MB")

                prevRemaining = remaining
            }

            // Write timestamp to output
            val writtenTimestamp: Int = if (timestamp < prevTimestamp) {
                prevWrittenTimestamp + 1
            } else {
                val diff = timestamp - prevTimestamp
                prevWrittenTimestamp + diff
            }

            output.writeInt(writtenTimestamp)
            fixedTimestampsOutput.writeBytes("$writtenTimestamp\n")

            prevTimestamp = timestamp
            prevWrittenTimestamp = writtenTimestamp

            // Write packet to output
            output.writeInt(packetLength)
            output.write(packet)
        }
    } catch (e: EOFException) {
        println("\nReached end of file")
    }
}