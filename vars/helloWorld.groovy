def call(String name, String job){
  sh "echo Hello World, I am ${name}"
  sh "echo and I'm a ${job}"
}

