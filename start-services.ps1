# Start-Services.ps1
# Script to launch all 5 microservices in the background using PowerShell jobs.

$backendDir = $PSScriptRoot

# List of services with their folder names and log basenames
$services = @(
    @{ Name = "Gateway"; Folder = "Gateway"; Port = 9003 },
    @{ Name = "AuthenticationMS"; Folder = "AuthenticationMS"; Port = 9001 },
    @{ Name = "FollowMS"; Folder = "FollowMS"; Port = 9002 },
    @{ Name = "PostManagementMS"; Folder = "PostManagementMS"; Port = 9004 },
    @{ Name = "TrendingPostsMS"; Folder = "trendingposts"; Port = 9005 }
)

Write-Host "Stopping any existing Java processes running on our microservice ports..."
foreach ($svc in $services) {
    $port = $svc.Port
    $proc = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
    if ($proc) {
        $pidToKill = $proc.OwningProcess
        Write-Host "Killing process $pidToKill on port $port"
        Stop-Process -Id $pidToKill -Force -ErrorAction SilentlyContinue
    }
}

Write-Host "Stopping any existing background jobs..."
Get-Job | Remove-Job -Force -ErrorAction SilentlyContinue

Write-Host "Starting services in the background..."
foreach ($svc in $services) {
    $name = $svc.Name
    $folder = $svc.Folder
    $workDir = Join-Path $backendDir $folder
    $logOut = Join-Path $backendDir "$($folder.ToLower()).log"
    $logErr = Join-Path $backendDir "$($folder.ToLower())-err.log"

    Write-Host "Starting $name in $workDir..."
    Start-Job -Name $name -ScriptBlock {
        param($dir, $out, $err)
        cd $dir
        cmd.exe /c ".\mvnw.cmd spring-boot:run > `"$out`" 2> `"$err`""
    } -ArgumentList $workDir, $logOut, $logErr
}

Write-Host "All services have been started in the background."
Write-Host "You can monitor their job status by running 'Get-Job'."
Write-Host "To view real-time console output, run: Get-Job -Name <ServiceName> | Receive-Job -Keep"
