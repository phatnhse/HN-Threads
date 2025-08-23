package com.phatnhse.hnthreads

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform