package pl.futurecollars.invoicing.service

import spock.lang.Specification

import java.nio.file.Files

class FileServiceUnitTest extends Specification {

    def tempPath = File.createTempFile("temp", ".txt").toPath()
    def fileService = new FileService()

    def "should write line correctly to file"() {
        given:
        def testLine = "this is testline"

        when:
        fileService.writeToFile(tempPath, testLine)

        then:
        testLine == Files.readAllLines(tempPath)[0]
    }

    def "should read line from file correctly"() {
        given:
        def testLine = "this is testline"

        when:
        fileService.writeToFile(tempPath, testLine)

        then:
        testLine == fileService.readLinesFromFile(tempPath)[0]

    }

    def "should save list of string lines correctly"() {
        given:
        def testList = ["a", "b", "c"]

        when:
        fileService.writeLinesToFile(tempPath, testList)

        then:
        testList == fileService.readLinesFromFile(tempPath)
    }

    def "should append to a file correctly"() {
        given:
        def testLine = "this is a test line"

        when:
        fileService.writeToFile(tempPath, testLine + "\n")
        fileService.appendToFile(tempPath, testLine)

        then:
        [testLine, testLine] == fileService.readLinesFromFile(tempPath)
    }

}
