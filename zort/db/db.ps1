$json = Get-Content -Raw -Path "./db/db.json"
$arr = $json | ConvertFrom-Json

foreach ($item in $arr) {
    Write-Output $item.name
}
