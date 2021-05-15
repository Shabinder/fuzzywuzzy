/*
 *  * Copyright (c)  2021  Shabinder Singh
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  *  You should have received a copy of the GNU General Public License
 *  *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.shabinder.fuzzywuzzy.diffutils.algorithms

import io.github.shabinder.fuzzywuzzy.Ratio
import io.github.shabinder.fuzzywuzzy.ToStringFunction

class TokenSet : RatioAlgorithm() {

    override fun apply(s1: String, s2: String, ratio: Ratio, stringFunction: ToStringFunction<String>): Int {
        var s1Copy = s1
        var s2Copy = s2

        s1Copy = stringFunction.apply(s1Copy)
        s2Copy = stringFunction.apply(s2Copy)

        val tokens1 = Utils.tokenizeSet(s1Copy)
        val tokens2 = Utils.tokenizeSet(s2Copy)

        val intersection = SetUtils.intersection(tokens1, tokens2)
        val diff1to2 = SetUtils.difference(tokens1, tokens2)
        val diff2to1 = SetUtils.difference(tokens2, tokens1)

        val sortedInter = Utils.sortAndJoin(intersection, " ").trim()
        val sorted1to2 = (sortedInter + " " + Utils.sortAndJoin(diff1to2, " ")).trim { it <= ' ' }
        val sorted2to1 = (sortedInter + " " + Utils.sortAndJoin(diff2to1, " ")).trim { it <= ' ' }

        val results = ArrayList<Int>()

        results.add(ratio.apply(sortedInter, sorted1to2))
        results.add(ratio.apply(sortedInter, sorted2to1))
        results.add(ratio.apply(sorted1to2, sorted2to1))

        return results.maxOrNull()!!

    }

}