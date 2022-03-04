package ca.uhn.fhir.jpa.starter.vo.agreement;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnPnstkeyTbo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TfnPnstkeyDto {

  private String pvsnInstCd;

  private String dtlSn;

  private String secKeyVal;

  private String useYn;

  private String yldYn;

  private String issuYmd;

  private String vldYmd;

  private String frstRegDt;

  private String frstRgtrId;

  private String frstRgtrIpAddr;

  private String lastMdfcnDt;

  private String lastMdfrId;

  private String lastMdfrIpAddr;

  @Builder
  public TfnPnstkeyDto(String pvsnInstCd, String dtlSn, String secKeyVal, String useYn, String yldYn, String issuYmd,
      String vldYmd, String frstRegDt, String frstRgtrId, String frstRgtrIpAddr, String lastMdfcnDt, String lastMdfrId,
      String lastMdfrIpAddr) {
    this.pvsnInstCd = pvsnInstCd;
    this.dtlSn = dtlSn;
    this.secKeyVal = secKeyVal;
    this.useYn = useYn;
    this.yldYn = yldYn;
    this.issuYmd = issuYmd;
    this.vldYmd = vldYmd;
    this.frstRegDt = frstRegDt;
    this.frstRgtrId = frstRgtrId;
    this.frstRgtrIpAddr = frstRgtrIpAddr;
    this.lastMdfcnDt = lastMdfcnDt;
    this.lastMdfrId = lastMdfrId;
    this.lastMdfrIpAddr = lastMdfrIpAddr;
  };
  
  /**
   * 
   * @param tfnPnstkeyTbo
   * @return
   */
  public static TfnPnstkeyDto from(TfnPnstkeyTbo tfnPnstkeyTbo) {
    return TfnPnstkeyDto.builder()
        .pvsnInstCd(tfnPnstkeyTbo.getPvsnInstCd())
        .dtlSn(tfnPnstkeyTbo.getDtlSn())
        .secKeyVal(tfnPnstkeyTbo.getSecKeyVal())
        .useYn(tfnPnstkeyTbo.getUseYn())
        .yldYn(tfnPnstkeyTbo.getYldYn())
        .issuYmd(tfnPnstkeyTbo.getIssuYmd())
        .vldYmd(tfnPnstkeyTbo.getVldYmd())
        .frstRegDt(tfnPnstkeyTbo.getFrstRegDt())
        .frstRgtrId(tfnPnstkeyTbo.getFrstRgtrId())
        .frstRgtrIpAddr(tfnPnstkeyTbo.getFrstRgtrIpAddr())
        .lastMdfcnDt(tfnPnstkeyTbo.getLastMdfcnDt())
        .lastMdfrId(tfnPnstkeyTbo.getLastMdfrId())
        .lastMdfrIpAddr(tfnPnstkeyTbo.getLastMdfrIpAddr())
        .build();
  }

}
