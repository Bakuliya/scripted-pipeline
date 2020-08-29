properties([
    parameters([
        string(defaultValue: '', description: 'Please enter VM IP', name: 'nodeIP', trim: true)
        ])
    ])
if (nodeIP.length() > 6) {
    node {
        stage('Pull Repo') {
            git branch: 'master', changelog: false, poll: false, url: 'https://github.com/spring-projects/spring-petclinic.git'
        }
        withEnv(['ANSIBLE_HOST_KEY_CHECKING=False', ' https://github.com/spring-projects/spring-petclinic.git', 'FLASKEX_BRANCH=master']) {
            stage("Install Prerequisites"){
                ansiblePlaybook credentialsId: 'Jenkins-master-ssh-key', inventory: '${nodeIP},', playbook: 'prerequisites.yml'
                }
            stage("Pull spring petclinic"){
                ansiblePlaybook credentialsId: 'Jenkins-master-ssh-key', inventory: '${nodeIP},', playbook: 'pull_repo.yml'
                }
            stage("Install 'java"){
                ansiblePlaybook credentialsId: 'Jenkins-master-ssh-key', inventory: '${nodeIP},', playbook: 'install_python.yml'
                }
            stage("Start playbook"){
                ansiblePlaybook credentialsId: 'Jenkins-master-ssh-key', inventory: '${nodeIP},', playbook: 'start_app.yml'
                }
        }
    }
}
else {
    error 'Please enter valid IP address'
}