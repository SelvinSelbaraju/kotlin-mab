# Kotlin Multi-Armed Bandit

The aim of this project is to learn the basics of Kotlin and Multi-Armed Bandits at the same time. 

Multi-Armed Bandits are a type of problem, where you have many options to choose from and you want to pick the options that maximise your reward. A key component of this type of problem is that the properties (eg. distribution of rewards) of the options (arms) is not known at the start. An agent must engage with the environment to learn these properties. There becomes a tradeoff of exploiting the option which seems to be the best, versus exploring other options and learning more about the environment.  

A Multi-Armed Bandit environment (to be defined by a config file) is simulated with various strategies. In particular, we try:
1. Epsilon Greedy 
2. Upper Confidence Bound (to do)
3. Thompson Sampling (to do)
4. Bayesian Bandits (to do)

By engaging in the environment through multiple trials, we will try to get an understanding of which strategies are best in certain styles of environments. For example, a certain strategy might perform better when the underlying distribution of arms is narrow or wide.

To do this, we need to define an evaluation metric. A sensible one would be to look at the average reward across trials for a given strategy for a given environment. We can then run hypothesis tests to see if these differences are statistically significant, and we can visualise distributions of reward, number of pulls for each arm etc.