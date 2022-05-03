library(ggplot2)

file <-read.csv("/Users/juri/development/operationsAssistant/recommender_engine/F_C25.csv", header = FALSE)
p <- ggplot(file, aes(fill=V2, x=V3, y=V1)) + 
  geom_violin() + labs(x="Fold",y="score",fill="metrics") + theme(legend.position="top", )
p
