package com.example.fake_slink.model.singleton

import android.util.Log
import com.example.fake_slink.model.response.ClassSubjectResponse
import com.example.fake_slink.model.response.GradeResponse
import com.example.fake_slink.model.response.LearningOutcomesResponse

object LearningOutcomes {
    private val TAG = "LEARNING_OUTCOMES"

    private var learningOutcomesResponse: LearningOutcomesResponse? = null

    fun login(classSubjectResponse: LearningOutcomesResponse) {
        this.learningOutcomesResponse = classSubjectResponse
    }

    fun logout() {
        learningOutcomesResponse = null
    }

    fun get(): LearningOutcomesResponse? {
        return learningOutcomesResponse
    }

    fun update(gradeResponse: GradeResponse){
        if(this.learningOutcomesResponse != null) {
            val gradeList = this.learningOutcomesResponse?.gradeResponseList?.toMutableList() ?: mutableListOf()

            val idx = gradeList.indexOfFirst {
                it.classSubjectResponse.classSubjectID == gradeResponse.classSubjectResponse.classSubjectID
            }

            if(idx != -1) {
                gradeList[idx] = gradeResponse
            } else {
                Log.e(TAG, "idx == -1")
            }

            this.learningOutcomesResponse?.gradeResponseList = gradeList
        }
    }
}