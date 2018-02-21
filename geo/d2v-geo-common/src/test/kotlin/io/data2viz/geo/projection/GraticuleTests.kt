package io.data2viz.geo.projection

import io.data2viz.geo.*
import io.data2viz.path.epsilon
import io.data2viz.test.TestBase
import kotlin.math.abs
import kotlin.test.Test

class GraticuleTests : TestBase() {

    @Test
    fun graticule_extent_sets_extendMinor_and_extentMajor_LEGACY() {
        val graticule = graticule()
        graticule.extent = Extent(90.0, -45.0, 90.0, 45.0)

        graticule.extentMajor.x0 shouldBe 90.0
        graticule.extentMajor.y0 shouldBe -45.0
        graticule.extentMajor.x1 shouldBe 90.0
        graticule.extentMajor.y1 shouldBe 45.0
        graticule.extentMinor.x0 shouldBe 90.0
        graticule.extentMinor.y0 shouldBe -45.0
        graticule.extentMinor.x1 shouldBe 90.0
        graticule.extentMinor.y1 shouldBe 45.0
    }

    @Test
    fun graticule_extent_gets_extendMinor_LEGACY() {
        val graticule = graticule()
        graticule.extentMinor = Extent(-90.0, -45.0, 90.0, 45.0)

        graticule.extent.x0 shouldBe -90.0
        graticule.extent.y0 shouldBe -45.0
        graticule.extent.x1 shouldBe 90.0
        graticule.extent.y1 shouldBe 45.0
    }

    @Test
    fun graticule_extentMajor_default_longitude_ranges_from_180W_inclusive_to_180E_inclusive_LEGACY() {
        val graticule = graticule().extentMajor

        graticule.x0 shouldBe -180.0
        graticule.x1 shouldBe 180.0
    }

    @Test
    fun graticule_extentMajor_default_latitude_ranges_from_90S_exclusive_to_90N_exclusive_LEGACY() {
        val graticule = graticule().extentMajor

        graticule.y0 shouldBeClose -90.0
        graticule.y1 shouldBeClose 90.0
    }

    @Test
    fun graticule_extentMinor_default_longitude_ranges_from_180W_inclusive_to_180E_inclusive_LEGACY() {
        val graticule = graticule().extentMinor

        graticule.x0 shouldBe -180.0
        graticule.x1 shouldBe 180.0
    }

    @Test
    fun graticule_extentMinor_default_latitude_ranges_from_80S_exclusive_to_80N_inclusive_LEGACY() {
        val graticule = graticule().extentMinor

        graticule.y0 shouldBeClose -80.0
        graticule.y1 shouldBeClose 80.0
    }

    @Test
    fun graticule_step_sets_minor_and_major_step_LEGACY() {
        val graticule = graticule()
        graticule.step = doubleArrayOf(22.5, 22.5)

        graticule.stepMinor shouldBe doubleArrayOf(22.5, 22.5)
        graticule.stepMajor shouldBeClose doubleArrayOf(22.5, 22.5)
    }

    @Test
    fun graticule_step_gets_the_minor_step_LEGACY() {
        val graticule = graticule()
        graticule.stepMinor = doubleArrayOf(22.5, 22.5)

        graticule.step shouldBe doubleArrayOf(22.5, 22.5)
    }

    @Test
    fun graticule_stepminor_defaults_to_10_10_LEGACY() {
        val graticule = graticule()
        graticule.stepMinor shouldBe doubleArrayOf(10.0, 10.0)
    }

    @Test
    fun graticule_stepmajor_defaults_to_90_360_LEGACY() {
        val graticule = graticule()
        graticule.stepMajor shouldBe doubleArrayOf(90.0, 360.0)
    }

    @Test
    fun graticule_lines_default_longitude_ranges_from_180W_inclusive_to_180E_exclusive_LEGACY() {
        val lines = graticule().lines()
            .filter { it.coordinates[0][0] == it.coordinates[1][0] }
            .sortedBy { it.coordinates[0][0] }

        lines[0].coordinates[0][0] shouldBe -180.0
        lines[lines.lastIndex].coordinates[0][0] shouldBe 170.0
    }

    @Test
    fun graticule_lines_default_latitude_ranges_from_90S_exclusive_to_90N_exclusive_LEGACY() {
        val lines = graticule().lines()
            .filter { it.coordinates[0][1] == it.coordinates[1][1] }
            .sortedBy { it.coordinates[0][1] }

        lines[0].coordinates[0][1] shouldBe -80.0
        lines[lines.lastIndex].coordinates[0][1] shouldBe 80.0
    }

    @Test
    fun graticule_lines_default_minor_longitude_lines_extend_from_80S_to_80N_LEGACY() {
        val lines = graticule().lines()
            .filter { it.coordinates[0][0] == it.coordinates[1][0] }
            .filter { abs(it.coordinates[0][0] % 90.0) > epsilon }

        lines.forEach {
            it.coordinates.minBy { it[1] }!![1] shouldBe (-80 - epsilon)
            it.coordinates.maxBy { it[1] }!![1] shouldBe (80 + epsilon)
        }
    }

    @Test
    fun graticule_lines_default_major_longitude_lines_extend_from_90S_to_90N_LEGACY() {
        val lines = graticule().lines()
            .filter { it.coordinates[0][0] == it.coordinates[1][0] }
            .filter { abs(it.coordinates[0][0] % 90.0) < epsilon }

        lines.forEach {
            it.coordinates.minBy { it[1] }!![1] shouldBe (-90 + epsilon)
            it.coordinates.maxBy { it[1] }!![1] shouldBe (90 - epsilon)
        }
    }

    @Test
    fun graticule_lines_default_latitude_lines_extend_from_180W_to_180E_LEGACY() {
        val lines = graticule().lines()
            .filter { it.coordinates[0][1] == it.coordinates[1][1] }

        lines.forEach {
            it.coordinates.minBy { it[0] }!![0] shouldBe -180.0
            it.coordinates.maxBy { it[0] }!![0] shouldBe 180.0
        }
    }

    @Test
    fun graticule_lines_returns_array_of_linestrings_LEGACY() {
        val graticule = graticule() {
            extent = Extent(-90.0, -45.0, 90.0, 45.0)
            step = doubleArrayOf(45.0, 45.0)
            precision = 3.0
        }
        val lines = graticule.lines()

        lines[0].coordinates[0] shouldBe doubleArrayOf(-90.0, -45.0)
        lines[0].coordinates[1] shouldBe doubleArrayOf(-90.0, 45.0)         // meridian
        lines[1].coordinates[0] shouldBe doubleArrayOf(-45.0, -45.0)
        lines[1].coordinates[1] shouldBe doubleArrayOf(-45.0, 45.0)         // meridian
        lines[2].coordinates[0] shouldBe doubleArrayOf(.0, -45.0)
        lines[2].coordinates[1] shouldBe doubleArrayOf(.0, 45.0)            // meridian
        lines[3].coordinates[0] shouldBe doubleArrayOf(45.0, -45.0)
        lines[3].coordinates[1] shouldBe doubleArrayOf(45.0, 45.0)          // meridian
        //{type: "LineString", coordinates: [[-90,-45],[-87,-45],[-84,-45],[-81,-45],[-78,-45],[-75,-45],[-72,-45],[-69,-45],[-66,-45],[-63,-45],[-60,-45],[-57,-45],[-54,-45],[-51,-45],[-48,-45],[-45,-45],[-42,-45],[-39,-45],[-36,-45],[-33,-45],[-30,-45],[-27,-45],[-24,-45],[-21,-45],[-18,-45],[-15,-45],[-12,-45],[-9,-45],[-6,-45],[-3,-45],[0,-45],[3,-45],[6,-45],[9,-45],[12,-45],[15,-45],[18,-45],[21,-45],[24,-45],[27,-45],[30,-45],[33,-45],[36,-45],[39,-45],[42,-45],[45,-45],[48,-45],[51,-45],[54,-45],[57,-45],[60,-45],[63,-45],[66,-45],[69,-45],[72,-45],[75,-45],[78,-45],[81,-45],[84,-45],[87,-45],[90,-45]]},
        //{type: "LineString", coordinates: [[-90,0],[-87,0],[-84,0],[-81,0],[-78,0],[-75,0],[-72,0],[-69,0],[-66,0],[-63,0],[-60,0],[-57,0],[-54,0],[-51,0],[-48,0],[-45,0],[-42,0],[-39,0],[-36,0],[-33,0],[-30,0],[-27,0],[-24,0],[-21,0],[-18,0],[-15,0],[-12,0],[-9,0],[-6,0],[-3,0],[0,0],[3,0],[6,0],[9,0],[12,0],[15,0],[18,0],[21,0],[24,0],[27,0],[30,0],[33,0],[36,0],[39,0],[42,0],[45,0],[48,0],[51,0],[54,0],[57,0],[60,0],[63,0],[66,0],[69,0],[72,0],[75,0],[78,0],[81,0],[84,0],[87,0],[90,0]]}
    }

    @Test
    fun graticule_graticule_returns_a_multistring_of_all_lines_LEGACY() {
        val g = graticule() {
            extent = Extent(-90.0, -45.0, 90.0, 45.0)
            step = doubleArrayOf(45.0, 45.0)
            precision = 3.0
        }
        val lines = g.graticule()

        lines.coordinates[0][0] shouldBe doubleArrayOf(-90.0, -45.0)
        lines.coordinates[0][1] shouldBe doubleArrayOf(-90.0, 45.0)         // meridian
        lines.coordinates[1][0] shouldBe doubleArrayOf(-45.0, -45.0)
        lines.coordinates[1][1] shouldBe doubleArrayOf(-45.0, 45.0)         // meridian
        lines.coordinates[2][0] shouldBe doubleArrayOf(.0, -45.0)
        lines.coordinates[2][1] shouldBe doubleArrayOf(.0, 45.0)            // meridian
        lines.coordinates[3][0] shouldBe doubleArrayOf(45.0, -45.0)
        lines.coordinates[3][1] shouldBe doubleArrayOf(45.0, 45.0)          // meridian
    }

    @Test
    fun graticule_outline_returns_a_polygon_encompassing_the_major_extent_LEGACY() {
        val g = graticule() {
            extentMajor = Extent(-90.0, -45.0, 90.0, 45.0)
            precision = 3.0

        }
        val outline = g.outline()

        outline.coordinates[0][0] shouldBe doubleArrayOf(-90.0, -45.0)
        outline.coordinates[0][1] shouldBe doubleArrayOf(-90.0, 45.0)         // meridian
//        outline.coordinates[0][2] shouldBe doubleArrayOf(-87.0, 45.0)
//        outline.coordinates[0][3] shouldBe doubleArrayOf(-84.0, 45.0)         // meridian
//        outline.coordinates[2][0] shouldBe doubleArrayOf(.0, -45.0)
//        outline.coordinates[2][1] shouldBe doubleArrayOf(.0, 45.0)            // meridian
//        outline.coordinates[3][0] shouldBe doubleArrayOf(45.0, -45.0)
//        outline.coordinates[3][1] shouldBe doubleArrayOf(45.0, 45.0)          // meridian
    }


    /*
tape("graticule.outline() returns a Polygon encompassing the major extent", function(test) {
  test.deepEqual(d3.geoGraticule()
      .extentMajor([[-90, -45], [90, 45]])
      .precision(3)
      .outline(), {
    type: "Polygon",
    coordinates: [[
      [-90,-45],[-90,45], // meridian
      [-87,45],[-84,45],[-81,45],[-78,45],[-75,45],[-72,45],[-69,45],[-66,45],[-63,45],[-60,45],[-57,45],[-54,45],[-51,45],[-48,45],[-45,45],[-42,45],[-39,45],[-36,45],[-33,45],[-30,45],[-27,45],[-24,45],[-21,45],[-18,45],[-15,45],[-12,45],[-9,45],[-6,45],[-3,45],[0,45],[3,45],[6,45],[9,45],[12,45],[15,45],[18,45],[21,45],[24,45],[27,45],[30,45],[33,45],[36,45],[39,45],[42,45],[45,45],[48,45],[51,45],[54,45],[57,45],[60,45],[63,45],[66,45],[69,45],[72,45],[75,45],[78,45],[81,45],[84,45],[87,45],
      [90,45],[90,-45], // meridian
      [87,-45],[84,-45],[81,-45],[78,-45],[75,-45],[72,-45],[69,-45],[66,-45],[63,-45],[60,-45],[57,-45],[54,-45],[51,-45],[48,-45],[45,-45],[42,-45],[39,-45],[36,-45],[33,-45],[30,-45],[27,-45],[24,-45],[21,-45],[18,-45],[15,-45],[12,-45],[9,-45],[6,-45],[3,-45],[0,-45],[-3,-45],[-6,-45],[-9,-45],[-12,-45],[-15,-45],[-18,-45],[-21,-45],[-24,-45],[-27,-45],[-30,-45],[-33,-45],[-36,-45],[-39,-45],[-42,-45],[-45,-45],[-48,-45],[-51,-45],[-54,-45],[-57,-45],[-60,-45],[-63,-45],[-66,-45],[-69,-45],[-72,-45],[-75,-45],[-78,-45],[-81,-45],[-84,-45],[-87,-45],[-90,-45]
    ]]
  });
  test.end();
});
     */
}