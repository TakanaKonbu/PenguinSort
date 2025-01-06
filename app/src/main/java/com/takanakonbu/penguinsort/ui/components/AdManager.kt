package com.takanakonbu.penguinsort.ui.components

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.takanakonbu.penguinsort.sound.SoundManager

class AdManager(
    private val activity: Activity,
    private val soundManager: SoundManager
) {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    init {
        preloadAds()
    }

    private fun preloadAds() {
        preloadRewardedAd()
        preloadInterstitialAd()
    }

    private fun preloadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            activity,
//            "ca-app-pub-2836653067032260/7922387934",
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    private fun preloadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            activity,
//            "ca-app-pub-2836653067032260/2470750706",
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    fun loadContinueAd(onAdDismissed: () -> Unit, onRewardEarned: () -> Unit) {
        if (rewardedAd != null) {
            setupRewardedCallback(onAdDismissed, onRewardEarned)
            soundManager.stopBgm() // BGMを停止
            rewardedAd?.show(
                activity,
                OnUserEarnedRewardListener {
                    onRewardEarned()
                }
            )
        } else {
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                activity,
//                "ca-app-pub-2836653067032260/7922387934",
                "ca-app-pub-3940256099942544/5224354917",
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardedAd = ad
                        setupRewardedCallback(onAdDismissed, onRewardEarned)
                        soundManager.stopBgm() // BGMを停止
                        ad.show(
                            activity,
                            OnUserEarnedRewardListener {
                                onRewardEarned()
                            }
                        )
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        rewardedAd = null
                        onAdDismissed()
                    }
                }
            )
        }
    }

    fun loadRetryAd(onAdDismissed: () -> Unit) {
        if (interstitialAd != null) {
            setupInterstitialCallback(onAdDismissed)
            interstitialAd?.show(activity)
        } else {
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                activity,
//                "ca-app-pub-2836653067032260/2470750706",
                "ca-app-pub-3940256099942544/1033173712",
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        interstitialAd = ad
                        setupInterstitialCallback(onAdDismissed)
                        ad.show(activity)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        interstitialAd = null
                        onAdDismissed()
                    }
                }
            )
        }
    }

    private fun setupInterstitialCallback(onAdDismissed: () -> Unit) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                preloadInterstitialAd()
                interstitialAd = null
                onAdDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                preloadInterstitialAd()
                interstitialAd = null
                onAdDismissed()
            }
        }
    }

    private fun setupRewardedCallback(onAdDismissed: () -> Unit, onRewardEarned: () -> Unit) {
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                preloadRewardedAd()
                rewardedAd = null
                soundManager.startBgm() // BGMを再開
                onAdDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                preloadRewardedAd()
                rewardedAd = null
                soundManager.startBgm() // BGMを再開
                onAdDismissed()
            }
        }
    }
}