# PowerShell script to generate a release keystore and configure build properties

$keystoreFile = "release.jks"
$propertiesFile = "keystore.properties"
$password = "AlphaTypePassword"
$alias = "AlphaTypeKey"

if (Test-Path $keystoreFile) {
    Write-Host "Keystore '$keystoreFile' already exists. Skipping generation." -ForegroundColor Yellow
} else {
    Write-Host "Generating release keystore..." -ForegroundColor Cyan
    
    # Check if keytool is available
    if (-not (Get-Command keytool -ErrorAction SilentlyContinue)) {
        Write-Error "keytool command not found. Please ensure Java JDK is installed and in your PATH."
        exit 1
    }

    keytool -genkey -v -keystore $keystoreFile -alias $alias -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=AlphaType, OU=Development, O=AlphaType, L=Mobile, S=Global, C=US" -storepass $password -keypass $password
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Keystore generated successfully: $keystoreFile" -ForegroundColor Green
    } else {
        Write-Error "Failed to generate keystore."
        exit 1
    }
}

if (-not (Test-Path $propertiesFile)) {
    Write-Host "Creating $propertiesFile..." -ForegroundColor Cyan
    $props = @"
storeFile=../release.jks
storePassword=$password
keyAlias=$alias
keyPassword=$password
"@
    $props | Out-File -FilePath $propertiesFile -Encoding ascii
    Write-Host "$propertiesFile created successfully." -ForegroundColor Green
} else {
    Write-Host "$propertiesFile already exists. Skipping creation." -ForegroundColor Yellow
}

Write-Host "`nReady to build! You can now run .\build_release.ps1" -ForegroundColor Green
