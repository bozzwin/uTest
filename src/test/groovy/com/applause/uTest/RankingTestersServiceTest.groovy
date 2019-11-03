package com.applause.uTest

import com.applause.uTest.domain.Device
import com.applause.uTest.domain.Tester
import com.applause.uTest.repository.BugRepository
import com.applause.uTest.repository.TesterRepository
import com.applause.uTest.service.RankingTestersService
import com.applause.uTest.service.ResultDTO
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class RankingTestersServiceTest extends Specification {

    TesterRepository testerRepository = Mock();
    BugRepository bugRepository = Mock();

    @Subject
    RankingTestersService service = new RankingTestersService(testerRepository, bugRepository)

    @Unroll
    def "should return ranking depend on country: #countries and devices: #devices"() {
        when:
            testerRepository.findAll() >> testersData(['ALL'])
            testerRepository.findByCountryIn([countries]) >> testersData([countries])
            bugRepository.countByTesteridAndDeviceidIn(testerId, deviceIds) >> score
            def outcomes = service.generateRanking([countries], [devices])
        then:
            outcomes.eachWithIndex { ResultDTO entry, int i ->
                assert entry.testerName == result[i].testerName
                assert entry.score == result[i].score
            }
        where:
            countries | devices     | testerId | deviceIds | score | result
            'JP'      | 'HTC One'   | 8        | [9]       | 33    | [new ResultDTO('Sean', 33)]
            'JP'      | 'ALL'       | 5        | [1, 6]    | 43    | [new ResultDTO('Mingquan', 43), new ResultDTO('Sean', 0)]
            'ALL'     | 'Nexus 4'   | 9        | [6]       | 77    | [new ResultDTO('Darshini', 77), new ResultDTO('Mingquan', 0), new ResultDTO('Michael', 0)]
            'US'      | 'Galaxy S4' | 4        | [5]       | 11    | [new ResultDTO('Taybin', 11), new ResultDTO('Darshini', 0)]
    }

    static List<Tester> testersData(List<String> countries) {
        List<Tester> testers = []
        if (countries.contains("ALL") || countries.contains("JP")) {
            testers.add(new Tester(id: 5, firstName: 'Mingquan', lastName: 'Zheng', country: 'JP', devices:
                [new Device(id: 1, description: 'iPhone 4'),
                 new Device(id: 6, description: 'Nexus 4')]
            ))
            testers.add(new Tester(id: 8, firstName: 'Sean', lastName: 'Wellington', country: 'JP', devices:
                [new Device(id: 10, description: 'iPhone 3'),
                 new Device(id: 9, description: 'HTC One')]
            ))
        }
        if (countries.contains("ALL") || countries.contains("US")) {
            testers.add(new Tester(id: 4, firstName: 'Taybin', lastName: 'Rutkin', country: 'US', devices:
                [new Device(id: 5, description: 'Galaxy S4'),
                 new Device(id: 3, description: 'iPhone 5')]
            ))
            testers.add(new Tester(id: 2, firstName: 'Michael', lastName: 'Lubavin', country: 'US', devices:
                [new Device(id: 3, description: 'iPhone 5'),
                 new Device(id: 6, description: 'Nexus 4')]
            ))
        }
        if (countries.contains("ALL") || countries.contains("GB")) {
            testers.add(new Tester(id: 9, firstName: 'Darshini', lastName: 'Thiagarajan', country: 'GB', devices:
                [new Device(id: 10, description: 'iPhone 3'),
                 new Device(id: 5, description: 'Galaxy S4'),
                 new Device(id: 6, description: 'Nexus 4')]
            ))
        }
        testers
    }
}
