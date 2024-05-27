package com.example.barcodescanner;

public class ReturnPostModal {
    public class DataModal {
        private String ID;
        private String MektupAdi;
        private int MusteriID;
        private String PostayaVerilecegiTarih;
        private String SiparisTutari;
        private String TakipKodu;
        private String Tarih;
        private String TelefonNumarasi;
        private String UyeAdi;

        // Getter ve Setter metodları
        // ...

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getMektupAdi() {
            return MektupAdi;
        }

        public void setMektupAdi(String mektupAdi) {
            MektupAdi = mektupAdi;
        }

        public int getMusteriID() {
            return MusteriID;
        }

        public void setMusteriID(int musteriID) {
            MusteriID = musteriID;
        }

        public String getPostayaVerilecegiTarih() {
            return PostayaVerilecegiTarih;
        }

        public void setPostayaVerilecegiTarih(String postayaVerilecegiTarih) {
            PostayaVerilecegiTarih = postayaVerilecegiTarih;
        }

        public String getSiparisTutari() {
            return SiparisTutari;
        }

        public void setSiparisTutari(String siparisTutari) {
            SiparisTutari = siparisTutari;
        }

        public String getTakipKodu() {
            return TakipKodu;
        }

        public void setTakipKodu(String takipKodu) {
            TakipKodu = takipKodu;
        }

        public String getTarih() {
            return Tarih;
        }

        public void setTarih(String tarih) {
            Tarih = tarih;
        }

        public String getTelefonNumarasi() {
            return TelefonNumarasi;
        }

        public void setTelefonNumarasi(String telefonNumarasi) {
            TelefonNumarasi = telefonNumarasi;
        }

        public String getUyeAdi() {
            return UyeAdi;
        }

        public void setUyeAdi(String uyeAdi) {
            UyeAdi = uyeAdi;
        }
    }

}
