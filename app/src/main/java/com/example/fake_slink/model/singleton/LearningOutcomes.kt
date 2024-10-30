package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.ClassSubjectResponse
import com.example.fake_slink.model.response.LearningOutcomesResponse

object LearningOutcomes {
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
}