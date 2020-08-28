properties([
    parameters([
        string(defaultValue: '', description: 'Please enter VM IP', name: 'nodeIP', trim: true),
        string(defaultValue: '', description: 'Please enter branch name', name: 'branch', trim: true)
        ])
    ])
if (nodeIP?.length()) {
    node {
        stage('Pull Repo') {
            git branch: 'master', changelog: false, poll: false, url: ''
        }
        withEnv(['ANSIBLE_HOST_KEY_CHECKING=False']) {
            stage("Install Apache"){
                ansiblePlaybook credentialsId: 'Jenkins-master-ssh-key', extras: '-e melodi_branch=${branch}', inventory: '${nodeIP},', playbook: 'main.yml'
                }
            }  
        }  
}
else {
    error 'Please enter valid IP address'
}