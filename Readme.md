# AI Integration and Cloud Deployment with Java

### Topics covered

- What is AI and why AI
- what is OpenAI and LLM
- Getting API keys of Different LLMs
- text generation using java
- sentimental Analysis Using API
- create a chatbot
- prompt Engineering

- Git and Github
- What is VPC Hosting
- AWS ec2 setup and Deployment
- Basic linux command on ec2

# Setup Git and Github

- install git for windows
- open terminal
- `git config --global user.email "youremail@gmail.com"`
- `git config --global user.name "your name"`
- `git add -A`
- `git commit -a -m "any message"`
- make a repository on github and copy repository url
- `git remote add origin your-repository-url`
- `git push -u origin master`


# What is AI and Why AI ?

AI simulates human intelligence  with computers.
This includes learning,reasoning,generate,analyse the sentiments etc.

# History of OpenAi  
### GPT -> Generative Pre-trained Transformer

- OpenAI's history begins in December 2015 when it was founded as a non-profit AI research organization with the goal of developing artificial general intelligence (AGI). The founders, including Sam Altman, Elon Musk, and others, pledged $1 billion to fund its research and ensure AGI benefits all of humanity, rather than being monopolized by a single company.
- ### Key Milestones:
- 2015: Founded as a non-profit AI research organization.
- 2018: Released the concept of Generative Pre-trained Transformer (GPT), a neural network for language generation.
- 2021: Launched DALL-E, an AI model that generates images from text prompts.
- November 2022: Released ChatGPT, a chatbot powered by GPT-3.5, which gained rapid popularity and became a key AI tool.
- Ongoing: Continuously updates and releases new GPT models, including GPT-4.5, and explores advancements in areas like multimodal AI and voice capabilities.

## AI,ML,FM,GenAI,Deepfake,Chatbots

Nueron -> Neurons are the fundamental unit of the nervous system specialized to transmit information to different parts of the body.

## LLMs stand for Large Language Models. These are advanced AI systems trained on massive datasets (text, code, and more) to understand and generate human-like language.

🔍 What Are LLMs?
An LLM (Large Language Model) is a deep learning model that:

Is trained on large volumes of text data

Uses transformer architecture (like GPT, BERT, T5)

Can understand, summarize, translate, predict, and generate text

🧠 How Do They Work?
LLMs learn patterns and relationships between words, phrases, and sentences. They don’t “understand” language like humans do, but they:

Analyze statistical relationships in text

Predict the next word or sentence based on input

Generate outputs that sound contextually relevant and coherent

🚀 Popular LLMs
Model	Creator	Use Cases
GPT-3 / GPT-4	OpenAI	Chatbots, text generation, coding
Gemini	Google DeepMind	Multimodal tasks, reasoning
Claude	Anthropic	Safe conversational AI
LLaMA	Meta	Open-source research
Mistral	Mistral AI	Lightweight, open-source

🔧 Core Capabilities
Chatbot interactions (e.g., ChatGPT)

Code generation

Document summarization

Translation

Text-based reasoning

Semantic search

💡 Real-World Applications
Education: Virtual tutors, automated grading

Business: Customer support, report generation

Programming: Code suggestion, bug fixing

Healthcare: Medical note summarization

Legal: Contract analysis, legal drafting

🛠️ LLM Inputs and Outputs
Input: Natural language prompt (e.g., "Explain black holes in simple terms")

Output: Natural language response (e.g., "A black hole is a region of space...")


## Http Methods

HTTP -> hypertext transfer protocol 

protocol -> set of rules
